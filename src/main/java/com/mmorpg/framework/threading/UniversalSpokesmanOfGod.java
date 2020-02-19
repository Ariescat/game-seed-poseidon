package com.mmorpg.framework.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 通用神的代言人，理论上我什么都能搞
 */
public abstract class UniversalSpokesmanOfGod implements ISpokesmanOfGod {

	private final static Logger log = LoggerFactory.getLogger(UniversalSpokesmanOfGod.class);

	/** 是否还在运行 */
	private volatile AtomicBoolean running = new AtomicBoolean(false);
	/** 绑定的线程(只有isService()返回为true时，才有绑定线程) */
	private Thread bindThread;

	private long lastExecuteTimeMillis = System.currentTimeMillis();

	private final IUndeadConfig config = new IUndeadConfig() {
		@Override
		public boolean isDead() {
			return bindThread != null && !bindThread.isAlive();
		}
	};

	@Override
	public void run() {
		int cost = 0;

		if (isService() && bindThread == null) {
			bindThread = Thread.currentThread();
			bindThread.setName(getName());
		}

		while (running.get()) {
			long startTimeMillis = System.currentTimeMillis();
			try {
				execute(running.get());
			} catch (Throwable t) {
				log.error(String.format("Exception occure when execute in %s", getName()), t);
			}

			updateLastExecuteTimeMillis();

			cost = (int) (lastExecuteTimeMillis - startTimeMillis);
			if (log.isWarnEnabled() && getProcessPeriod() > 0 && cost > getProcessPeriod()) {
				log.warn("{} cost to much time ({})ms to execute", getName(), cost);
			}

			if (getMinSleepMills() > 0) {
				try {
					int sleepMillis = 0;
					if (getProcessPeriod() <= 0) {
						sleepMillis = getMinSleepMills();
					} else {
						sleepMillis = getProcessPeriod() - cost;
						if (startTimeMillis < getMinSleepMills()) {
							sleepMillis = getMinSleepMills();
						}
					}
					if (sleepMillis > 0) {
						Thread.sleep(sleepMillis);
					}
				} catch (InterruptedException e) {
					log.error(bindThread.getName(), e);
				}
			}
		}

		log.info("Stop {} which name is {}", getClass(), getName());
	}

	public abstract int getMinSleepMills();

	/**
	 * 处理请求的周期：毫秒（<=0表示不限制处理时间）
	 */
	public abstract int getProcessPeriod();

	/**
	 * 这个方法会在每个周期被调用一次
	 */
	public abstract void execute(boolean running);

	@Override
	public void stop() {
		running.compareAndSet(true, false);
	}

	@Override
	public IUndeadConfig getUndeadConfig() {
		return config;
	}

	@Override
	public boolean isService() {
		return true;
	}

	@Override
	public boolean isRunning() {
		return running.get();
	}

	@Override
	public Thread getBindThread() {
		return bindThread;
	}

	@Override
	public boolean setBindThread(Thread bindThread) {
		this.bindThread = bindThread;
		return true;
	}

	@Override
	public void init() {
		running.set(true);
		bindThread = null;
		updateLastExecuteTimeMillis();
	}

	public long getLastExecuteTimeMillis() {
		return lastExecuteTimeMillis;
	}

	public void updateLastExecuteTimeMillis() {
		lastExecuteTimeMillis = System.currentTimeMillis();
	}

}
