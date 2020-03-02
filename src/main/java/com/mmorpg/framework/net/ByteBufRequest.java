package com.mmorpg.framework.net;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.ReferenceCounted;

/**
 * @author Ariescat
 * @version 2020/2/27 20:22
 */
public class ByteBufRequest implements Request, ReferenceCounted {

	private ByteBuf byteBuf;
	private short packetId;
	private int byteSize;

	public ByteBufRequest(ByteBuf byteBuf) {
		this.byteBuf = byteBuf;
		this.packetId = byteBuf.readShort();
		this.byteSize = byteBuf.readableBytes();
	}

	public ByteBufRequest(ByteBuf byteBuf, short packetId) {
		this.byteBuf = byteBuf;
		this.packetId = packetId;
		this.byteSize = byteBuf.readableBytes();
	}

	public ByteBufRequest(ByteBuf byteBuf, Channel channel) {
		this.byteBuf = byteBuf;
		this.packetId = byteBuf.readShort();
		this.byteSize = byteBuf.readableBytes();
		resetPacketId(channel);
	}

	private void resetPacketId(Channel channel) {
		// TODO
	}

	@Override
	public short getPacketId() {
		return packetId;
	}

	@Override
	public int getByteSize() {
		return byteSize;
	}

	@Override
	public ByteBuf getByteBuf() {
		return byteBuf;
	}

	@Override
	public byte readByte() {
		return byteBuf.readByte();
	}

	@Override
	public short readShort() {
		return byteBuf.readShort();
	}

	@Override
	public int readInt() {
		return byteBuf.readInt();
	}

	@Override
	public long readLong() {
		return byteBuf.readLong();
	}

	@Override
	public float readFloat() {
		return byteBuf.readFloat();
	}

	@Override
	public String readString() {
		short length = byteBuf.readShort();
		byte[] content = new byte[length];
		byteBuf.readBytes(content);
		return new String(content, Charsets.UTF_8);
	}

	@Override
	public void readBytes(byte[] dst) {
		byteBuf.readBytes(dst);
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
