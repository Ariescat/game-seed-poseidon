package com.mmorpg.framework.asyncdb;

import com.mmorpg.framework.thread.threads.CoreThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class SyncQueuePool {

	private final static Logger log = LoggerFactory.getLogger(SyncQueuePool.class);

	private SyncQueue[] pool;

	private int poolSize;

	private ScheduledExecutorService monitor;

	private final ExecutorService workerExecutors;

	private volatile boolean stop;

	public SyncQueuePool(ThreadFactory threadFactory, int poolSize, ExceptionCallback callback, ISyncStrategy syncStrategy) {
		this.workerExecutors = Executors.newFixedThreadPool(poolSize, threadFactory);
		this.pool = new SyncQueue[poolSize];
		this.poolSize = poolSize;

		for (int i = 0; i < poolSize; i++) {
			this.pool[i] = new SyncQueue(i, callback, syncStrategy);
			this.workerExecutors.execute(this.pool[i]);
		}

		this.monitor = Executors.newScheduledThreadPool(1, new CoreThreadFactory("SyncDBMonitor"));
		this.monitor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				int totalWaiting = 0;
				for (SyncQueue each : pool) {
					SyncStats stats = each.stats();
					log.info("Queue: {}, SyncCount:{}, WaitingSize:{}, PeriodNum:{}", each.getQueueId(),
						stats.getTotal(), stats.getWaiting(), stats.getPeriodNum());
				}
				if (totalWaiting > 500) {
					log.warn("TotalWaiting: {}", totalWaiting);
				}
			}
		}, 0, 60L, TimeUnit.SECONDS);
	}

	public boolean submit(AsynDBEntity synchronizable) {
		if (this.stop) {
			return false;
		}

		SyncQueue sq = synchronizable.getSyncQueue();
		if (sq == null) {
			int hash = synchronizable.getHash() % this.poolSize;
			hash = Math.abs(hash);
			sq = pool[hash];
			synchronizable.setSyncQueue(sq);
		}

		return sq.submit(synchronizable);
	}

	public boolean shutdown(long millis) throws InterruptedException {
		stop = true;

		for (int i = 0; i < poolSize; i++) {
			pool[i].shutdown(millis);
		}

		workerExecutors.shutdown();
		workerExecutors.awaitTermination(millis, TimeUnit.MILLISECONDS);
		monitor.shutdown();
		return true;
	}
}
