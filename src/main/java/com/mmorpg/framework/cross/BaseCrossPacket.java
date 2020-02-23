package com.mmorpg.framework.cross;

import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.AbstractPacket;
import com.mmorpg.logic.base.domain.Player;

/**
 * @author Ariescat
 * @version 2020/2/23 10:53
 */
public abstract class BaseCrossPacket extends AbstractPacket {

	@Override
	public PacketExE executeBeforeLogin(GameSession session) {
		return null;
	}

	@Override
	public PacketExE execute(Player session) {
		return null;
	}

}
