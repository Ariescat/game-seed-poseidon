package com.mmorpg.framework.net.handler;

import com.mmorpg.framework.utils.ChannelUtils;
import com.mmorpg.framework.utils.OnlinePlayer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ariescat
 * @version 2020/2/27 20:07
 */
public class IPCountHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String ip = ChannelUtils.getIP(ctx.channel());
		AtomicInteger count = OnlinePlayer.getInstance().getSameIPCount(ip);
		count.incrementAndGet();
		super.channelActive(ctx);
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String ip = ChannelUtils.getIP(ctx.channel());
		AtomicInteger count = OnlinePlayer.getInstance().getSameIPCount(ip);
		count.decrementAndGet();
		super.channelInactive(ctx);
	}
}
