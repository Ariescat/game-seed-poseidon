package com.mmorpg.framework.net.handler;

import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.AbstractPacket;
import com.mmorpg.framework.utils.ChannelUtils;
import com.mmorpg.logic.base.scene.creature.player.Player;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Ariescat
 * @version 2020/2/19 14:50
 */
public class GameServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		GameSession session = ChannelUtils.getChannelSession(ctx.channel());
		Player player = session.getPlayer();
		player.addPacket((AbstractPacket) msg);
	}
}
