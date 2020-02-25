package com.mmorpg.framework.cross;

import com.mmorpg.framework.rpc.msg.ICrossBaseMsg;
import com.mmorpg.framework.rpc.msg.ICrossPlayerMsg;
import com.mmorpg.framework.rpc.msg.packet.CrossMsgPacket;
import io.netty.channel.Channel;

/**
 * @author Ariescat
 * @version 2020/2/23 10:18
 */
public enum RemoteServers {

	XXXCross,
	;

	public static void sendCrossMsg(CrossClient crossClient, ICrossBaseMsg<?> msg) {
//		TODO
//		crossClient.sendPacket(createCrossMsgPacket(msg));
	}

	public static void sendCrossMsg(Channel channel, ICrossBaseMsg<?> msg) {
		channel.writeAndFlush(createCrossMsgPacket(msg));
	}

	private static Object createCrossMsgPacket(ICrossBaseMsg<?> msg) {
		CrossMsgPacket packet = CrossMsgPacket.of(msg);
		long playerId = 0;
		if (msg instanceof ICrossPlayerMsg) {
			playerId = ((ICrossPlayerMsg) msg).getPlayerId();
		}
		return new DispatchPacket(playerId, packet);
	}
}
