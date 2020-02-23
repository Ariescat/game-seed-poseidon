package com.mmorpg.framework.rpc.msg.sender;

import com.mmorpg.framework.cross.RemoteServers;
import com.mmorpg.framework.rpc.msg.ICrossBaseMsg;
import io.netty.channel.Channel;

/**
 * @author Ariescat
 * @version 2020/2/23 13:54
 */
public class ChannelCrossMsgSender implements ICrossMsgSender {

	private final Channel channel;

	public ChannelCrossMsgSender(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void sendMsg(ICrossBaseMsg<?> msg) {
		RemoteServers.sendCrossMsg(channel, msg);
	}
}
