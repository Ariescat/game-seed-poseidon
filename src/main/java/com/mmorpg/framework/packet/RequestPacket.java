package com.mmorpg.framework.packet;

import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.net.session.GameSession;

/**
 * @author Ariescat
 * @version 2020/2/25 16:13
 */
public abstract class RequestPacket extends AbstractPacket {

	@Override
	public PacketExE executeBeforeLogin(GameSession session) {
		return null;
	}

	@Override
	public void doResponse(Response response) {

	}
}
