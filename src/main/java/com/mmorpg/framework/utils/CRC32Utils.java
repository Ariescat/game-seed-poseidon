package com.mmorpg.framework.utils;

import java.util.zip.CRC32;

/**
 * @author Ariescat
 * @version 2020/3/2 17:50
 */
public class CRC32Utils {

	private static final ThreadLocal<CRC32> crc32 = new ThreadLocal<CRC32>() {
		@Override
		protected CRC32 initialValue() {
			return new CRC32();
		}
	};

	public static long calCRC32(byte[] array, int off, int length) {
		CRC32 crc32 = CRC32Utils.crc32.get();
		crc32.update(array, off, length);
		return crc32.getValue();
	}

	public static long calCRC32(byte[] array) {
		return calCRC32(array, 0, array.length);
	}
}
