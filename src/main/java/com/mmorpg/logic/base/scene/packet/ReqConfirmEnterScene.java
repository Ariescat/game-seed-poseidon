package com.mmorpg.logic.base.scene.packet;

import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.RequestPacket;
import com.mmorpg.framework.packet.anno.Packet;
import com.mmorpg.logic.base.scene.creature.player.Player;

/**
 * 该协议处理过后，玩家才会被加入某个具体的场景对象中，并且玩家消息处理进入场景循环
 *
 * @author Ariescat
 * @version 2020/3/3 11:21
 */
@Packet(commandId = PacketId.REQ_CONFIRM_ENTER_SCENE, cross = true)
public class ReqConfirmEnterScene extends RequestPacket {

	@Override
	public void read(Request request) throws Exception {

	}

	/**
	 * 第一次进场景时才会执行
	 */
	@Override
	public PacketExE executeBeforeLogin(GameSession session) {
		// TODO
		return super.executeBeforeLogin(session);
	}

	/**
	 * 此后场景切换执行
	 */
	@Override
	public PacketExE execute(Player player) {
		return null;
	}
}
