package com.mmorpg.framework.net.handler;

import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.CloseCause;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.AbstractPacket;
import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.utils.ChannelUtils;
import com.mmorpg.framework.utils.ExceptionUtils;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.login.GateKeepers;
import com.mmorpg.logic.base.scene.creature.player.Player;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ariescat
 * @version 2020/2/28 10:23
 */
@Slf4j
public class GameAuthHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("channelActive {}", ctx.channel());
		if (!ChannelUtils.addChannelSession(ctx.channel(), new GameSession(ctx.channel()))) {
			ctx.channel().close();
			log.error("duplicate session! IP:{}", ChannelUtils.getIP(ctx.channel()));
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("channelInactive {}, Account:{}", ctx.channel(), ChannelUtils.getAccount(ctx.channel()));
		GameSession gameSession = ChannelUtils.getChannelSession(ctx.channel());
		if (gameSession == null) {
			return;
		}
		Player player = gameSession.getPlayer();
		if (player != null && player.isCrossed()) {
			// TODO 处理跨服
			Context.it().crossClientManager.crossExit(player);
			player.getCrossInfo().clear();
		}
		if (gameSession.isInit()) {
			// 请求策略文件的SOCKET，直接移除
			ChannelUtils.removeChannelSession(gameSession.getChannel());
			return;
		}
		GateKeepers.requestExitworld(gameSession);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		GameSession session = ChannelUtils.getChannelSession(ctx.channel());
		if (session == null) {
			return;
		}

		Request request = (Request) msg;
		AbstractPacket packet = null;
		try {
			packet = PacketFactory.decodePacket(request, session);
		} catch (Exception e) {
			ExceptionUtils.log(e);
		} finally {
			ReferenceCountUtil.release(request);
		}

		if (packet == null) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("Recv [{}, {}], {}, {}, size:{}, {}", session.getAccount(), session.getUid(),
				packet.getCommand(), packet.getClass().getSimpleName(), request.getByteSize(), ctx.channel());
		}

		if (session.isCanProcessPacketInScene()) {
			ctx.fireChannelRead(packet);
			return;
		}

		log.error("Player[{}, {}], Status Exception: {}, Command: {}",
			session.getUid(), session.getAccount(), session.getStatus(), packet.getCommand());
		session.close(CloseCause.Status_Exception);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		log.error("exceptionCaught! IP:{}, Account:{}", ChannelUtils.getIP(ctx.channel()), ChannelUtils.getAccount(channel));
		if (channel.isOpen() || channel.isActive()) {
			channel.close();
		}
	}
}
