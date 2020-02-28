package com.mmorpg.framework.utils;

import io.netty.buffer.ByteBuf;

import static org.apache.commons.codec.Charsets.UTF_8;

/**
 * @author Ariescat
 * @version 2020/2/27 20:29
 */
public class ByteBufUtils {

	public static String readString(ByteBuf byteBuf) {
		short length = byteBuf.readShort();
		byte[] content = new byte[length];
		byteBuf.readBytes(content);
		return new String(content, UTF_8);
	}

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

	/**
	 * 动态方式写正整数
	 */
	public static void writePositiveInt(ByteBuf byteBuf, int value) {
		// TODO
	}

	public static int readPositiveInt(ByteBuf byteBuf) {
		// TODO
		return 0;
	}
}
