package com.mmorpg.framework.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用神的代言人，理论上我什么都能搞
 */
public abstract class UniversalSpokesmanOfGod implements ISpokesmanOfGod {

	private final static Logger LOGGER = LoggerFactory.getLogger(UniversalSpokesmanOfGod.class);

	/** 是否还在运行 */
	private volatile boolean running = true;
	/** 绑定的线程(只有isService()返回为true时，才有绑定线程) */
	private Thread bindThread;

	private long lastExecuteTimeMillis = System.currentTimeMillis();



	@Override
	public void run() {
		long cost = 0;
		long sleepMillis = 0;
		long startTimeMillis = System.currentTimeMillis();

		if (isService() && bindThread == null) {
			bindThread = Thread.currentThread();
			bindThread.setName(getName());
		}

		while (running) {
			startTimeMillis = System.currentTimeMillis();

			try {
				execute(running);
			} catch (Throwable t) {
				LOGGER.error(String.format("Exception occure when execute in %s", getName()), t);
			}

			lastExecuteTimeMillis = System.currentTimeMillis();

			cost = lastExecuteTimeMillis - startTimeMillis;
			if (getWarnCost() > 0 && cost > getWarnCost()) {
				LOGGER.warn("{} cost to much time ({})ms to execute", getName(), cost);
			}

			sleepMillis = calSleepMillis(cost);
			if (sleepMillis > 0) {
				try {
					Thread.sleep(sleepMillis);
				} catch (InterruptedException e) {
					LOGGER.error("", e);
				}
			}
		}
		try {
			onStop();
		} catch (Exception e) {
			LOGGER.error("Error occure when process scene's stop.", e);
		}
		LOGGER.info("Stop {} which name is {}", getClass(), getName());
	}

	/**
	 * @param cost
	 * @return
	 */
	protected abstract long calSleepMillis(long cost);

	protected abstract int getWarnCost();

	/**
	 *
	 */
	protected void onStop() {
	}

	/**
	 * 这个方法会在每个周期被调用一次
	 */
	public abstract void execute(boolean running);

	@Override
	public void stop() {
		running = false;
	}



	@Override
	public boolean isService() {
		return true;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	public Thread getBindThread() {
		return bindThread;
	}

	@Override
	public void init() {
		running = true;
		bindThread = null;
	}

	public long getLastExecuteTimeMillis() {
		return lastExecuteTimeMillis;
	}

	public void updateLastExecuteTimeMillis() {
		lastExecuteTimeMillis = System.currentTimeMillis();
	}

}
