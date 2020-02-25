package com.mmorpg.framework.packet;

import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.logic.base.domain.Player;

/**
 * @author Ariescat
 * @version 2020/2/25 16:12
 */
public abstract class ResponsePacket extends AbstractPacket {

	@Override
	public void read(Request request) throws Exception {
	}

	@Override
	public PacketExE executeBeforeLogin(GameSession session) {
		return null;
	}

	@Override
	public PacketExE execute(Player player) {
		return null;
	}
}
