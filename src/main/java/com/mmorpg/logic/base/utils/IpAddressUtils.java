package com.mmorpg.logic.base.utils;

/**
 * IP 地址工具类
 *
 * @author Ariescat
 * @version 2020/2/19 14:40
 */
public class IpAddressUtils {

	/**
	 * 将127.0.0.1 格式的地址转换为K整形HASH值
	 */
	public static long hashCode(String ipAddress) {
		String[] splits = ipAddress.split("\\.");
		long ipValue = 0;
		int offset = 24;
		for (String item : splits) {
			long part = Long.valueOf(item);
			ipValue += (part << offset);
			offset -= 8;
		}
		return ipValue;
	}

	/**
	 * 将127.0.0.1 格式的地址和端口转换为长整TEHASH值
	 */
	public static long hashCode(String ipAddress, int port) {
		return hashCode(ipAddress) + port;
	}

}
