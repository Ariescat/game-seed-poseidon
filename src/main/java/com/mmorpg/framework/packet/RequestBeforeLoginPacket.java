package com.mmorpg.framework.packet;

import com.mmorpg.framework.net.Response;
import com.mmorpg.logic.base.scene.creature.player.Player;

/**
 * @author Ariescat
 * @version 2020/3/3 10:59
 */
public abstract class RequestBeforeLoginPacket extends AbstractPacket {

	@Override
	public PacketExE execute(Player player) {
		throw new RuntimeException(this.getClass().getName() + "can not run here!!");
	}

	@Override
	public void doResponse(Response response) {

	}
}
