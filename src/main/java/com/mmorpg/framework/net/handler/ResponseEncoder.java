package com.mmorpg.framework.net.handler;

import com.mmorpg.framework.net.ByteBufResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Ariescat
 * @version 2020/2/28 9:57
 */
public class ResponseEncoder extends MessageToByteEncoder<ByteBufResponse> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, ByteBufResponse byteBufResponse, ByteBuf byteBuf) throws Exception {

	}
}
