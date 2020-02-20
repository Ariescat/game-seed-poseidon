package com.mmorpg.framework.asyncdb;

public interface ISyncStrategy {

	/**
	 * 每轮循环休眠多久
	 */
	long getSleepTime(int waitingSize);

	int getNumEachLoop();

	int getTryTime();
}
