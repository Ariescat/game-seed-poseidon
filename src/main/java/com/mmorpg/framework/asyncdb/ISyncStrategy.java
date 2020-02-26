package com.mmorpg.framework.asyncdb;

public interface ISyncStrategy {

	/**
	 * 每轮循环休眠多久
	 */
	long getSleepTime(int waitingSize);

	/**
	 * 每轮循环同步多少个
	 */
	int getNumEachLoop();

	/**
	 * 同步失败的重试次数
	 */
	int getTryTime();
}
