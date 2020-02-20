package com.mmorpg.framework.asyncdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicLong;

public class SyncQueue implements Runnable {

	private final static Logger log = LoggerFactory.getLogger(SyncQueue.class);

	private static AsynDBEntity SHUTDOWN_ENTITY = new AsynDBEntity();

	private final int queueId;

	private volatile boolean stop = false;

	private volatile AtomicLong syncCount = new AtomicLong();

	private final BlockingQueue<AsynDBEntity> syncQueue = new LinkedTransferQueue<AsynDBEntity>();

	private ExceptionCallback callback;

	private ISyncStrategy syncStrategy;

	private volatile long preNum;

	public SyncQueue(int queueId, ExceptionCallback callback, ISyncStrategy syncStrategy) {
		this.callback = callback;
		this.queueId = queueId;
		this.syncStrategy = syncStrategy;
	}

	public boolean submit(AsynDBEntity synchronizable) {
		if (this.stop) {
			return false;
		}
		return this.syncQueue.add(synchronizable);
	}

	public boolean shutdown(long millis) throws InterruptedException {
		if (this.stop) {
			return false;
		}
		this.stop = true;
		this.syncQueue.add(SHUTDOWN_ENTITY);
		return true;
	}

	public int getWaitingSize() {
		return syncQueue.size();
	}

	public long getSyncCount() {
		return syncCount.get();
	}

	public int getQueueId() {
		return queueId;
	}

	public SyncStats stats() {
		long total = this.getSyncCount();
		long periodNum = total - this.preNum;
		this.preNum = total;
		int waiting = getWaitingSize();
		return new SyncStats(waiting, total, periodNum);
	}

	@Override
	public void run() {
		while (true) {
			int numEachLoop = this.syncStrategy.getNumEachLoop();
			int tryTime = this.syncStrategy.getTryTime();

			for (int i = 0; i < numEachLoop; i++) {
				AsynDBEntity entity = null;
				try {
					entity = this.syncQueue.take();
				} catch (InterruptedException e) {
					log.error("SyncQueueInterrupterException {}", queueId, e);
				}
				if (entity == null || entity == SHUTDOWN_ENTITY) {
					break;
				}
				try {
					entity.trySync(tryTime);
					if (syncCount.get() == Long.MAX_VALUE) {
						syncCount.weakCompareAndSet(Long.MAX_VALUE, 0);
					}
					syncCount.incrementAndGet();
				} catch (Exception e) {
					callback.onException(e);
				}
			}

			if (this.stop) {
				if (this.syncQueue.isEmpty()) {
					// 要求停服，但同步队列非空，则不休眠，快速提交所有同步对象
					continue;
				}
				break;
			} else {
				// 没有停服，通过休眠来控制同步速率
				try {
					int waitingSize = this.syncQueue.size();
					long sleeptime = this.syncStrategy.getSleepTime(waitingSize);
					Thread.sleep(sleeptime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
