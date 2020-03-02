package com.mmorpg.framework.utils;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static org.apache.commons.codec.Charsets.UTF_8;

/**
 * @author Ariescat
 * @version 2020/2/27 20:29
 */
public class ByteBufUtils {

	private static int SEVEN_BIT_MASK = 0B01111111;
	private static int SYMBOL_BIT_MASK = ~SEVEN_BIT_MASK;

	/**
	 * 写长字符申，32767 个字节
	 */
	public static void writeString(ByteBuf byteBuf, String str) {
		if (str != null) {
			byte[] content = str.getBytes(UTF_8);
			byteBuf.writeShort(content.length);
			byteBuf.writeBytes(content);
		} else {
			byteBuf.writeShort(0);
		}
	}

	public static String readString(ByteBuf byteBuf) {
		short length = byteBuf.readShort();
		byte[] content = new byte[length];
		byteBuf.readBytes(content);
		return new String(content, UTF_8);
	}

	public static void write48bit(ByteBuf byteBuf, long value) {
		Preconditions.checkArgument(value < Constant.MAX_48BIT && value > Constant.MAX_48BIT_NEGATIVE);
		byteBuf.writeShort((int) (value >>> 32));
		byteBuf.writeInt((int) value);
	}

	public static long read48bit(ByteBuf byteBuf) {
		long high = byteBuf.readShort();
		long low = byteBuf.readUnsignedInt();
		return (high << 32) | low;
	}

	/**
	 * 动态方式写正整数，以符号位来表示需要多少个字节
	 *
	 * @param value >= 2的7次方  用两个字节
	 *              >= 2的14次方 用三个字节
	 *              >= 2的21次方 用四个字节
	 *              >= 2的28次方 用五个字节
	 */
	public static void writePositiveInt(ByteBuf out, int value) {
		int buf1 = value & SEVEN_BIT_MASK;
		int buf2 = (value >>> 7);
		if (buf2 == 0) {
			out.writeByte(buf1);
			return;
		}
		out.writeByte(buf1 | SYMBOL_BIT_MASK);

		int buf3 = (value >>> 14);
		if (buf3 == 0) {
			out.writeByte(buf2);
			return;
		}
		out.writeByte(buf2 | SYMBOL_BIT_MASK);

		int buf4 = (value >>> 21);
		if (buf4 == 0) {
			out.writeByte(buf3);
			return;
		}
		out.writeByte(buf3 | SYMBOL_BIT_MASK);

		int buf5 = (value >>> 28);
		if (buf5 == 0) {
			out.writeByte(buf4);
			return;
		}
		out.writeByte(buf4 | SYMBOL_BIT_MASK);
		out.writeByte(buf5);
	}

	public static int readPositiveInt(ByteBuf in) {
		int buf1 = 0;
		int buf2 = 0;
		int buf3 = 0;
		int buf4 = 0;
		int buf5 = 0;

		buf1 = in.readUnsignedByte();
		if ((buf1 & SYMBOL_BIT_MASK) != 0) {
			buf1 &= SEVEN_BIT_MASK;
			buf2 = in.readUnsignedByte();
			if ((buf2 & SYMBOL_BIT_MASK) != 0) {
				buf2 &= SEVEN_BIT_MASK;
				buf3 = in.readUnsignedByte();
				if ((buf3 & SYMBOL_BIT_MASK) != 0) {
					buf3 &= SEVEN_BIT_MASK;
					buf4 = in.readUnsignedByte();
					if ((buf4 & SYMBOL_BIT_MASK) != 0) {
						buf4 &= SEVEN_BIT_MASK;
						buf5 = in.readUnsignedByte();
						buf5 &= SEVEN_BIT_MASK;
					}
				}
			}
		}
		return buf1 + (buf2 << 7) + (buf3 << 14) + (buf4 << 21) + (buf5 << 28);
	}

	public static void main(String[] args) {
		ByteBuf buffer = Unpooled.buffer(64);

		writePositiveInt(buffer, 1234567898);
		System.err.println(readPositiveInt(buffer));
	}
}
