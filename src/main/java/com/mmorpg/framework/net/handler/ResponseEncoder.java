package com.mmorpg.framework.net.handler;

import com.mmorpg.framework.net.ByteBufResponse;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.utils.ByteBufUtils;
import com.mmorpg.framework.utils.ChannelUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ariescat
 * @version 2020/2/28 9:57
 */
@Slf4j
public class ResponseEncoder extends MessageToByteEncoder<ByteBufResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBufResponse response, ByteBuf out) throws Exception {
		ByteBuf byteBuf = response.getByteBuf();
		short command = response.getPacketId();

		if (log.isDebugEnabled()) {
			GameSession session = ChannelUtils.getChannelSession(ctx.channel());
			String commandStr = PacketFactory.getPacketNameByCommonId(command);
			log.debug("Send To [{}, {}], {}, {}, size:{}, {}", session.getAccount(), session.getUid(),
				command, commandStr, byteBuf.readableBytes(), ctx.channel());
		}

		if (command < 10000) {
			out.writeBytes(BaseFrameDecode.FRAME_MAGIC);
			out.writeInt(byteBuf.readableBytes());
		} else {
			ByteBufUtils.writePositiveInt(byteBuf, byteBuf.readableBytes());
		}
		out.writeBytes(byteBuf, 0, byteBuf.readableBytes());
	}
}
