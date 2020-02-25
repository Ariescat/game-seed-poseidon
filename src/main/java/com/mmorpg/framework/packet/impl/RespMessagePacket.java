package com.mmorpg.framework.packet.impl;

import com.mmorpg.framework.message.MessageId;
import com.mmorpg.framework.message.MessageType;
import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.ResponsePacket;
import com.mmorpg.framework.packet.anno.Packet;

/**
 * @author Ariescat
 * @version 2020/2/19 12:32
 */
@Packet(commandId = PacketId.RESP_MESSAGE)
public class RespMessagePacket extends ResponsePacket {

	public void init(MessageType messageType, MessageId msgId, Object[] args) {

	}

	@Override
	public void doResponse(Response response) {

	}
}
