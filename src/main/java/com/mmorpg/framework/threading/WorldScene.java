package com.mmorpg.framework.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 世界场景--处理公共业务
 */
public class WorldScene {

	private final static Logger log = LoggerFactory.getLogger(WorldScene.class);

	private final static WorldScene instance = new WorldScene();

	/** BOSS线程池：我负责管理Worker服务 */
	private final static ExecutorService bossThreadPool = Executors.newCachedThreadPool();
	/** 线程池中存在的BOSS服务 */
	private ConcurrentLinkedQueue<ISpokesmanOfGod> bossServices = new ConcurrentLinkedQueue<ISpokesmanOfGod>();

	/** Worker线程池：负责处理每个向世界的业务请求 */
	private final static ExecutorService workerThreadPool = Executors.newCachedThreadPool();
	/** 线程池中存在的Worker服务 */
	private ConcurrentLinkedQueue<ISpokesmanOfGod> workerServices = new ConcurrentLinkedQueue<ISpokesmanOfGod>();

	public AtomicBoolean isStop = new AtomicBoolean(false);

	private WorldScene() {
	}

	public static WorldScene getInstance() {
		return instance;
	}

	public void executeAngle() {

	}

	public void executeWorker(ISpokesmanOfGod spokesmanOfGod) {
		if (isStop.get()) return;

		spokesmanOfGod.init();
		synchronized (workerServices) {
			if (spokesmanOfGod.isService()) {
				workerServices.add(spokesmanOfGod);
			}
		}
		workerThreadPool.execute(spokesmanOfGod);
	}

	public void executeWorker(ISpokesmanOfGod[] spokesmanOfGods) {
		for (ISpokesmanOfGod spokesmanOfGod : spokesmanOfGods) {
			try {
				executeWorker(spokesmanOfGod);
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	public void removeWorker(ISpokesmanOfGod spokesmanOfGod) {
		synchronized (workerServices) {
			workerServices.remove(spokesmanOfGod);
		}
	}

	public void stop() {
		if (isStop.compareAndSet(false, true)) {
			stopBossThread();
			stopWorkerThreads();
		}
	}

	private void stopBossThread() {
		stopThreads("BossThread", bossThreadPool, bossServices);
	}

	private void stopWorkerThreads() {
		stopThreads("WorkerThread", workerThreadPool, workerServices);
	}

	private void stopThreads(String name, ExecutorService pool, ConcurrentLinkedQueue<ISpokesmanOfGod> services) {
		for (ISpokesmanOfGod cleric : services) {
			cleric.stop();
			removeWorker(cleric);
		}
		pool.shutdown();
		try {
			pool.awaitTermination(30 * (services.size() + 1), TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			List<Runnable> shutdownNow = pool.shutdownNow();
			if (shutdownNow.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (Runnable a : shutdownNow) {
					if (a instanceof ISpokesmanOfGod) {
						sb.append("\t").append(((ISpokesmanOfGod) a).getName()).append("\n");
					}
				}
				log.error("世界场景强制关闭了[" + name + "]以下服务---：\n" + sb);
			} else {
				log.info("世界场景成功关闭[" + name + "]");
			}
		}
	}
}
