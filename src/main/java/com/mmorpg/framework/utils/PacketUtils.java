package com.mmorpg.framework.utils;

import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.ResponsePacket;
import com.mmorpg.framework.packet.impl.RespLoginConflictPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author Ariescat
 * @version 2020/2/19 12:33
 */
public class PacketUtils {

	public static void sendAndClose(Channel channel, ResponsePacket packet) {
		Response response = packet.write();
		channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static ChannelFuture sendChannelIdle(Channel channel) {
		RespLoginConflictPacket packet = PacketFactory.createPacket(PacketId.CROSS_PROTO_STUFF_PACKET);
		packet.idle();
		Response response = packet.write();
		return channel.writeAndFlush(response);
	}
}
