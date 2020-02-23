package com.mmorpg.framework.thread.business;

import com.mmorpg.framework.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ariescat
 * @version 2020/2/18 23:52
 */
public abstract class AbstractBusinessService implements IBusinessService, InitializingBean {

	protected final static Logger log = LoggerFactory.getLogger(AbstractBusinessService.class);

	/**
	 * 消息队列
	 */
	private BlockingQueue<IBusinessMessage> messages = new LinkedBlockingQueue<>();
	/**
	 * 状态
	 */
	private AtomicBoolean running = new AtomicBoolean(false);
	/**
	 * 消费线程
	 */
	private Thread consumerThread = null;

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public Thread getThread() {
		return consumerThread;
	}

	@Override
	public void start() {
		if (running.compareAndSet(false, true)) {
			consumerThread = new Thread(this, this.getName());
			consumerThread.start();
		}
	}

	@Override
	public void stop() {
		if (running.compareAndSet(true, false)) {
			try {
				consumerThread.join();
			} catch (InterruptedException e) {
				ExceptionUtils.log(e);
			}
		}
		log.warn("{} stop", getName());
	}

	public boolean pushMessage(IBusinessMessage message) {
		if (running.get()) {
			if (consumerThread == Thread.currentThread()) {
				message.execute();
				return true;
			}
			return messages.offer(message);
		}
		return false;
	}

	@Override
	public void run() {
		while (running.get() || !messages.isEmpty()) {
			int count = 0;
			try {
				while (running.get() && count < getProcessNumPerPeriod()) {
					IBusinessMessage message = messages.poll();
					if (message == null) {
						break;
					}
					try {
						message.execute();
					} catch (Throwable e) {
						ExceptionUtils.log(e);
					}
					count++;
				}
				this.execute();
				Thread.sleep(getMinSleepMillis());
			} catch (Throwable e) {
				ExceptionUtils.log(e);
			}
		}
	}

	private long getMinSleepMillis() {
		return 100L;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.start();
	}
}
