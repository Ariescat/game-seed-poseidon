package com.mmorpg.framework.net.handler;

import com.mmorpg.framework.utils.PacketUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ariescat
 * @version 2020/2/28 10:25
 */
@Slf4j
public class GameServerIdleHandler extends ChannelDuplexHandler {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE) {
				ChannelFuture future = PacketUtils.sendChannelIdle(ctx.channel());
				future.addListener(ChannelFutureListener.CLOSE);
				log.warn("Channel Idle timeout: {}", ctx.channel().remoteAddress().toString());
			}
		}
	}
}
