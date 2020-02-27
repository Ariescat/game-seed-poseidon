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
	 *
	 * @param buffer
	 */
	public static void writeString(ByteBuf buffer, String str) {
		if (str != null) {
			byte[] content = str.getBytes(UTF_8);
			buffer.writeShort(content.length);
			buffer.writeBytes(content);
		} else {
			buffer.writeShort(0);
		}
	}
}
