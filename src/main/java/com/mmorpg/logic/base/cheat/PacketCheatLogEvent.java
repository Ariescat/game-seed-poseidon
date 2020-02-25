package com.mmorpg.logic.base.cheat;

import com.mmorpg.framework.event.Event;
import com.mmorpg.framework.utils.TimeUtils;

/**
 * @author Ariescat
 * @version 2020/2/19 15:23
 */
public class PacketCheatLogEvent implements Event {

	protected String account;
	protected String server;
	protected long time;
	private int type;

	public PacketCheatLogEvent(String account, String server, int type) {
		super();
		this.account = account;
		this.server = server;
		this.type = type;
		this.time = TimeUtils.getCurrentMillisTime();
	}

	public long getTime() {
		return time;
	}

	public StringBuilder message() {
		StringBuilder sb = new StringBuilder(256);
		sb.append("account:").append(account);
		sb.append(",server:").append(server);
		sb.append(",time:").append(time);
		sb.append(",type:").append(type);
		return sb;
	}
}
