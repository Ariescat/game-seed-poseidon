package com.mmorpg.framework.threading;

import com.mmorpg.framework.threading.policy.IRequestOverflowPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 精灵：神代言人的一种
 * 我能在固定时间周期处理固定数目的请求
 * <p>
 * 在getProcessPeriod() - getMinSleepMills()时间段内不断取任务执行，最多执行getProcessNumPerPeriod()个
 */
public abstract class Sprite extends UniversalSpokesmanOfGod {

	private final static Logger log = LoggerFactory.getLogger(Sprite.class);

	private final ConcurrentLinkedQueue<IRequestToGod> requests = new ConcurrentLinkedQueue<>();
	/**
	 * 当前请求数目，取size的话需要遍历，因此多维护一个变量
	 */
	private final AtomicInteger currentRequestNum = new AtomicInteger();

	public void add(IRequestToGod request) {
		if (isRunning()) {
			if (currentRequestNum.incrementAndGet() < getMaxRequestNum()) {
				requests.add(request);
			} else {
				currentRequestNum.decrementAndGet();
				log.error("{} request overflow, currentSize:{},maxRequestNum:{}", getName(), requests.size(), getMaxRequestNum());
				IRequestOverflowPolicy overflowPolicy = getOverflowPolicy();
				if (overflowPolicy != null) {
					overflowPolicy.handle(this, request);
				}
			}
		}
	}

	@Override
	public void execute(boolean running) {
		int processCount = 0;
		IRequestToGod poll = null;
		long timeOut = System.currentTimeMillis() + getProcessPeriod() - getMinSleepMills();
		while (running && isNumNotOut(processCount) && (getProcessPeriod() <= 0 || System.currentTimeMillis() < timeOut)) {
			poll = poll();
			if (poll == null || !running)
				break;

			boolean isCount = true;
			try {
				isCount = poll.execute();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				updateLastExecuteTimeMillis();
				if (isCount) {
					processCount++;
				}
			}
		}
	}

	public abstract IRequestOverflowPolicy getOverflowPolicy();

	public abstract int getMaxRequestNum();

	public abstract int getProcessNumPerPeriod();

	private boolean isNumNotOut(int count) {
		return getProcessNumPerPeriod() <= 0 || count < getProcessNumPerPeriod();
	}

	public IRequestToGod poll() {
		IRequestToGod poll = requests.poll();
		if (poll != null) {
			currentRequestNum.decrementAndGet();
		}
		return poll;
	}

	public void addDirectly(IRequestToGod request) {
		if (isRunning()) {
			currentRequestNum.incrementAndGet();
			requests.add(request);
		}
	}

	public int getRequestCount() {
		return requests.size();
	}

	@Override
	public void init() {
		super.init();
		requests.clear();
		currentRequestNum.set(0);
	}
}
