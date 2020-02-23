package com.mmorpg.framework.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author Ariescat
 * @version 2020/2/19 12:22
 */
public class Timer {

	private long nextTime;
	private long interval;

	public Timer(long initialDelay, long period, TimeUnit unit) {
		this.nextTime = TimeUtils.getCurrentMillisTime() + unit.toMillis(initialDelay);
		this.interval = unit.toMillis(period);
	}

	public Timer(long period, TimeUnit unit) {
		this(0, period, unit);
	}

	public boolean isTimeOut(long utime) {
		if (utime > nextTime) {
			nextTime = utime + interval;
			return true;
		}
		return false;
	}

	public void changeInterval(long interval) {
		this.interval = interval;
	}

	public void reset(long random) {
		nextTime = TimeUtils.getCurrentMillisTime() + random;
	}

	public void reset() {
		nextTime = TimeUtils.getCurrentMillisTime() + interval;
	}

	public void clear() {
		nextTime = TimeUtils.getCurrentMillisTime();
	}

	public long getNextTime() {
		return this.nextTime;
	}

}
