package com.mmorpg.framework.net.handler;

import com.mmorpg.framework.net.ByteBufRequest;
import com.mmorpg.framework.utils.ByteBufUtils;
import com.mmorpg.logic.base.Context;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ariescat
 * @version 2020/2/28 9:55
 */
@Slf4j
public class BaseFrameDecode extends ByteToMessageDecoder {

	private static final int MAX_BODY_SIZE = 1024 * 1024;
	public static final byte[] FRAME_MAGIC = {5, 5, 2, 7};
	public static final byte[] FRAME_MAGIC_CROSS = {'c', 'r', 'o', 's'};

	private static final int FRAME_STATUS_NEED_HEADER = 0;
	private static final int FRAME_STATUS_NEED_BODY = 1;
	private static final int FRAME_STATUS_NEED_CROSS = 2;

	private int frameStatus;
	private int bodySize;

	private boolean isMatch(byte[] src, byte[] target) {
		if (src.length != target.length) {
			return false;
		}
		for (int i = 0; i < target.length; i++) {
			if (src[i] != target[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
		switch (frameStatus) {
			case FRAME_STATUS_NEED_HEADER:
				// 后面要读取一个int长度（32bit）的bodySize，所以最小长度为 FRAME_MAGIC.length + 4
				if (in.readableBytes() < FRAME_MAGIC.length + 4) {
					return;
				}

				byte[] magics = new byte[FRAME_MAGIC.length];
				in.readBytes(magics);
				if (isMatch(magics, FRAME_MAGIC)) {
					bodySize = in.readInt();
					frameStatus = FRAME_STATUS_NEED_BODY;
				} else if (isMatch(magics, FRAME_MAGIC_CROSS)) {
					bodySize = in.readInt();
					frameStatus = FRAME_STATUS_NEED_CROSS;
				} else {
					throw new CorruptedFrameException("BaseFrameDecode Magic Header Error: " + Arrays.toString(magics));
				}


			case FRAME_STATUS_NEED_BODY:
				if (bodySize > MAX_BODY_SIZE) {
					throw new RuntimeException("BaseFrameDecode bodySize too large! " + bodySize);
				}
				if (in.readableBytes() < bodySize) {
					return;
				}

				ByteBuf byteBuf = ctx.alloc().heapBuffer(bodySize);
				byteBuf.writeBytes(in, bodySize);
				list.add(new ByteBufRequest(byteBuf, ctx.channel()));
				frameStatus = FRAME_STATUS_NEED_HEADER;
				return;

			case FRAME_STATUS_NEED_CROSS:
				if (bodySize > MAX_BODY_SIZE) {
					throw new RuntimeException("BaseFrameDecode bodySize too large! " + bodySize);
				}
				if (in.readableBytes() < bodySize) {
					return;
				}

				String authKey = ByteBufUtils.readString(in);
				String platform = ByteBufUtils.readString(in);
				int serverId = in.readInt();
				String configAuthKey = Context.it().configService.getCrossAuthKey();
				if (!configAuthKey.equals(authKey)) {
					log.error("BaseFrameDecode cross key not match {} {}", authKey, configAuthKey);
					ctx.close();
					return;
				}
				// cache channel
				Context.it().crossChannelManager.putCrossChannel(platform, serverId, ctx.channel());

				// remove handlers
				ChannelPipeline pipeline = ctx.pipeline();
				pipeline.remove("IdleStateHandler");
				pipeline.remove("WriteTimeoutHandler");
				pipeline.remove("GameServerIdleHandler");
				pipeline.remove("BaseFrameDecode");
				pipeline.remove("IPCountHandler");
				pipeline.remove("GameAuthHandler");
				pipeline.remove("GameServerHandler");
				pipeline.remove("ResponseEncoder");

				// add handlers TODO

				return;

			default:
				throw new Error("BaseFrameDecode frameStatus Error: " + frameStatus);
		}
	}
}
