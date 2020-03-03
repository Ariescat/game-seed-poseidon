package com.mmorpg.framework.packet.impl;

import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.ResponsePacket;
import com.mmorpg.framework.packet.anno.Packet;

/**
 * @author Ariescat
 * @version 2020/2/19 12:02
 */
@Packet(commandId = PacketId.RESP_LOGIN_ASK)
public class RespLoginAskPacket extends ResponsePacket {

	@Override
	public void doResponse(Response response) {

	}
}
