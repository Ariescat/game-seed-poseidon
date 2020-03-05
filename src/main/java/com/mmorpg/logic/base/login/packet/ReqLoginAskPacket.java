package com.mmorpg.logic.base.login.packet;

import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.RequestBeforeLoginPacket;
import com.mmorpg.framework.packet.anno.Packet;
import com.mmorpg.framework.utils.PacketUtils;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.login.GateKeepers;
import com.mmorpg.logic.base.scene.creature.player.Player;

/**
 * 验证playerId（具体角色）的登录
 *
 * @author Ariescat
 * @version 2020/3/3 10:57
 */
@Packet(commandId = PacketId.REQ_LOGIN_ASK)
public class ReqLoginAskPacket extends RequestBeforeLoginPacket {

	private long playerId;

	@Override
	public void read(Request request) throws Exception {
		this.playerId = request.readLong();
	}

	@Override
	public PacketExE executeBeforeLogin(GameSession session) {
		// TODO 是否封禁 验证防沉迷 角色合法 等

		RespLoginAskPacket packet = PacketFactory.createPacket(PacketId.RESP_LOGIN_ASK);
		Player player = Context.it().playerService.getPlayerOrCreate(playerId);
		if (player != null) {
			GateKeepers.processLogin(player, session, packet);
		} else {
			assert packet != null;
			PacketUtils.sendAndClose(session, packet.fail());
		}
		return null;
	}

}
