package com.mmorpg.framework.cross;

import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.rpc.msg.ICrossBaseMsg;
import com.mmorpg.framework.rpc.msg.packet.CrossMsgPacket;
import io.netty.channel.Channel;

/**
 * @author Ariescat
 * @version 2020/2/23 10:18
 */
public enum RemoteServers {

	ACross,
	;

	public static void sendCrossMsg(Channel channel, ICrossBaseMsg<?> msg) {
		channel.writeAndFlush(createCrossMsgPacket(msg));
	}

	private static Object createCrossMsgPacket(ICrossBaseMsg<?> msg) {
		CrossMsgPacket packet = PacketFactory.createPacket(PacketId.CROSS_PROTO_STUFF_PACKET);
		return null;
	}
}
