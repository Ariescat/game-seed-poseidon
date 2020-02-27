package com.mmorpg.framework.net.handler;

import com.mmorpg.framework.utils.ChannelUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetAddress;

/**
 * @author Ariescat
 * @version 2020/2/27 20:07
 */
public class IPLimitHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String ip = ChannelUtils.getIP(ctx.channel());
		InetAddress address = InetAddress.getLocalHost();
		String localIP = address.getHostAddress();
		if (!localIP.equals(ip)) {
			ctx.close();
			return;
		}
		super.channelActive(ctx);
	}

}
