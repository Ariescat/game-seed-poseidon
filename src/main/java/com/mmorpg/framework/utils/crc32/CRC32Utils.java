package com.mmorpg.framework.utils.crc32;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * @author Ariescat
 * @version 2020/3/2 17:50
 */
public class CRC32Utils {

	private static final ThreadLocal<Checksum> crc32 = new ThreadLocal<Checksum>() {
		@Override
		protected Checksum initialValue() {
//			return new CRC32(); // java.util
//			return new Crc32c(); // io.netty.handler.codec.compression.Crc32c 但是类权限用不了
			return new PureJavaCrc32(); // copy from hadoop-common 据说性能更高
		}
	};

	public static long calCRC32(byte[] array, int off, int length) {
		Checksum sum = crc32.get();
		sum.reset();
		sum.update(array, off, length);
		return sum.getValue();
	}

	public static long calCRC32(byte[] array) {
		return calCRC32(array, 0, array.length);
	}

	public static void main(String[] args) {
		byte[] src = new byte[]{4, 1, 3, 6, 7, 8, 12, 43, 45, 21, 0, 32, 99, 56, 3, 4, 2};

		PureJavaCrc32 pureJavaCrc32 = new PureJavaCrc32();
		pureJavaCrc32.update(src, 0, src.length);
		System.err.println(pureJavaCrc32.getValue());

		CRC32 crc32 = new CRC32();
		crc32.update(src, 0, src.length);
		System.err.println(crc32.getValue());
	}
}
