package com.mmorpg.logic.base.login.packet;

import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.ResponsePacket;
import com.mmorpg.framework.packet.anno.Packet;

/**
 * @author Ariescat
 * @version 2020/3/3 14:31
 */
@Packet(commandId = PacketId.RESP_LOGIN_AUTH)
public class RespLoginAuthPacket extends ResponsePacket {

	@Override
	public void doResponse(Response response) {

	}
}
