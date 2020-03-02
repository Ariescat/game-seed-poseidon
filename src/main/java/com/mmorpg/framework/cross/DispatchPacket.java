package com.mmorpg.framework.cross;

import com.mmorpg.framework.rpc.msg.packet.CrossMsgPacket;
import io.netty.buffer.ByteBuf;

/**
 * @author Ariescat
 * @version 2020/2/25 16:32
 */
public class DispatchPacket {

	public DispatchPacket(long playerId, CrossMsgPacket packet) {
	}

	public DispatchPacket(long uid, short packetId, ByteBuf byteBuf) {
		// TODO
	}
}
