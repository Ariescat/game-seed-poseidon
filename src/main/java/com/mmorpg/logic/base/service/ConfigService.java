package com.mmorpg.logic.base.service;

import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/19 15:05
 */
@Component
public class ConfigService {

	/**
	 * 消息队列大于此值时关闭链接
	 */
	private static final int closeQueueSize = 50;

	/**
	 * 允许最大在线数量
	 */
	public int getMaxOnlineCount() {
		return 0;
	}

	public boolean isFlushStatus() {
		return false;
	}

	public long getPlugInterval() {
		return 0;
	}

	public int getPlugThreshold() {
		return 0;
	}

	/**
	 * 当前服务器是否开服
	 */
	public String getOriServerFlag() {
		return null;
	}

	public boolean isPacketSizeToClose(int size) {
		return size > closeQueueSize;
	}
}
