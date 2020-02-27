package com.mmorpg.logic.base.service;

import org.springframework.stereotype.Component;

import java.util.Set;

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
	 * 允许最大同IP数
	 */
	private static final int maxSameIpCount = 6;
	/**
	 * IP登陆上限白名单
	 */
	private Set<String> sameIpWhiteList;

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

	public int getMaxSameIpCount() {
		return maxSameIpCount;
	}

	public boolean isInSameIpWhiteList(String ip) {
		return sameIpWhiteList.contains(ip);
	}
}
