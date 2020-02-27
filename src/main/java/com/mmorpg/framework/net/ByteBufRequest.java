package com.mmorpg.framework.net;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;

import java.nio.channels.Channel;

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
		return 0;
	}

	@Override
	public int getByteSize() {
		return 0;
	}

	@Override
	public byte readByte() {
		return 0;
	}

	@Override
	public short readShort() {
		return 0;
	}

	@Override
	public int readInt() {
		return 0;
	}

	@Override
	public long readLong() {
		return 0;
	}

	@Override
	public float readFloat() {
		return 0;
	}

	@Override
	public String readString() {
		return null;
	}

	@Override
	public ByteBuf getByteBuf() {
		return null;
	}

	@Override
	public void readBytes(byte[] dst) {

	}

	@Override
	public int refCnt() {
		return 0;
	}

	@Override
	public ReferenceCounted retain() {
		return null;
	}

	@Override
	public ReferenceCounted retain(int increment) {
		return null;
	}

	@Override
	public boolean release() {
		return false;
	}

	@Override
	public boolean release(int decrement) {
		return false;
	}
}
