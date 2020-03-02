package com.mmorpg.framework.cross;

import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;
import lombok.Getter;

/**
 * TODO 有空研究一下ReferenceCounted
 *
 * @author Ariescat
 * @version 2020/2/25 16:32
 */
public class DispatchPacket implements ReferenceCounted {

	@Getter
	private long playerId;
	@Getter
	private short packetId;
	@Getter
	private ByteBuf byteBuf;

	public DispatchPacket(long playerId, short packetId, ByteBuf byteBuf) {
		this.playerId = playerId;
		this.packetId = packetId;
		this.byteBuf = byteBuf;
	}

	public DispatchPacket(long playerId, AbstractPacket packet) {
		this.playerId = playerId;
		ByteBuf byteBuf = packet.write().getByteBuf();
		this.packetId = byteBuf.readShort();
		this.byteBuf = byteBuf;
	}

	public DispatchPacket(long playerId, Response response) {
		this.playerId = playerId;
		/*
		 * 拷贝一份，不然下面的readShort后会改变愿来的ByteBuf，导致发给其他玩家的消息包不完整，没有消息号
		 */
		ByteBuf ori = response.getByteBuf();
		ByteBuf buffer = Unpooled.buffer(ori.readableBytes());
		ori.getBytes(0, buffer);
		this.packetId = buffer.readShort();
		this.byteBuf = buffer;
	}

	public void write(ByteBuf out) {
		out.writeInt(1 + 8 + 2 + byteBuf.readableBytes());
		out.writeByte(CrossMsgType.MSG);
		out.writeLong(playerId);
		out.writeShort(packetId);
		out.writeBytes(byteBuf);
	}

	@Override
	public int refCnt() {
		return byteBuf.refCnt();
	}

	@Override
	public ReferenceCounted retain() {
		return byteBuf.retain();
	}

	@Override
	public ReferenceCounted retain(int increment) {
		return byteBuf.retain(increment);
	}

	@Override
	public boolean release() {
		return byteBuf.release();
	}

	@Override
	public boolean release(int decrement) {
		return byteBuf.release(decrement);
	}
}
