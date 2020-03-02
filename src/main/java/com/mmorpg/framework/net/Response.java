package com.mmorpg.framework.net;

import io.netty.buffer.ByteBuf;

/**
 * @author Ariescat
 * @version 2020/2/19 14:08
 */
public interface Response {

	short getPacketId();

	ByteBuf getByteBuf();

	int getIndex();

	Response writeByte(int value);

	Response writeShort(int value);

	Response writeInt(int value);

	Response writeLong(long value);

	Response writeBytes(byte[] bytes);

	Response writeBytes(ByteBuf src);

	Response writeBytes(byte[] src, int srcIndex, int length);

	Response writeFloat(float value);

	Response writeDouble(double value);

	Response writeString(String value);

	Response write48bit(long value);
}
