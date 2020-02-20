package com.mmorpg.framework.asyncdb;

public class SyncStats {

	private int waiting;

	private long total;

	private long periodNum;

	public SyncStats(int waiting, long total, long periodNum) {
		this.waiting = waiting;
		this.total = total;
		this.periodNum = periodNum;
	}

	public int getWaiting() {
		return waiting;
	}

	public long getTotal() {
		return total;
	}

	public long getPeriodNum() {
		return periodNum;
	}
}
