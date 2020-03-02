package com.mmorpg.framework.cross.client;

import com.mmorpg.framework.cross.CrossType;

/**
 * @author Ariescat
 * @version 2020/2/19 14:39
 */
public class CrossInfo {

	private CrossType crossType;
	private String platform;
	private int serverId;
	private CrossStatus crossStatus;

	private enum CrossStatus {
		Normal, Crossing, Crossed;
	}

	public boolean isNormal() {
		return crossStatus == CrossStatus.Normal;
	}

	public boolean isCrossing() {
		return crossStatus == CrossStatus.Crossing;
	}

	public boolean isCrossed() {
		return crossStatus == CrossStatus.Crossed;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public CrossType getCrossType() {
		return crossType;
	}

	public void setCrossType(CrossType crossType) {
		this.crossType = crossType;
	}

	public void clear() {
		crossStatus = CrossStatus.Normal;
		platform = "";
		serverId = 0;
	}
}
