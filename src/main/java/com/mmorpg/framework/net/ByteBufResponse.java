package com.mmorpg.framework.net;

import com.mmorpg.framework.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;

/**
 * @author Ariescat
 * @version 2020/2/27 20:22
 */
public class ByteBufResponse implements Response, ReferenceCounted {

	private ByteBuf byteBuf;

	public ByteBufResponse() {
		this.byteBuf = Unpooled.buffer(64);
	}

	public ByteBufResponse(int packetId) {
		this.byteBuf = Unpooled.buffer(64);
		this.byteBuf.writeShort(packetId);
	}

	@Override
	public short getPacketId() {
		return byteBuf.getShort(0);
	}

	@Override
	public ByteBuf getByteBuf() {
		return byteBuf;
	}

	@Override
	public int getIndex() {
		return byteBuf.writerIndex();
	}

	@Override
	public Response writeByte(int value) {
		byteBuf.writeByte(value);
		return this;
	}

	@Override
	public Response writeShort(int value) {
		byteBuf.writeShort(value);
		return this;
	}

	@Override
	public Response writeInt(int value) {
		byteBuf.writeInt(value);
		return this;
	}

	@Override
	public Response writeLong(long value) {
		byteBuf.writeLong(value);
		return this;
	}

	@Override
	public Response writeBytes(byte[] src) {
		byteBuf.writeBytes(src);
		return this;
	}

	@Override
	public Response writeBytes(byte[] src, int srcIndex, int length) {
		byteBuf.writeBytes(src, srcIndex, length);
		return this;
	}

	@Override
	public Response writeBytes(ByteBuf src) {
		byteBuf.writeBytes(src);
		return this;
	}

	@Override
	public Response writeFloat(float value) {
		byteBuf.writeFloat(value);
		return this;
	}

	@Override
	public Response writeDouble(double value) {
		byteBuf.writeDouble(value);
		return this;
	}

	@Override
	public Response writeString(String value) {
		ByteBufUtils.writeString(byteBuf, value);
		return this;
	}

	@Override
	public Response write48bit(long value) {
		ByteBufUtils.write48bit(byteBuf, value);
		return this;
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
