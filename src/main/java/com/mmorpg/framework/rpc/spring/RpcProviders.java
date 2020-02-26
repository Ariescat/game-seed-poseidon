package com.mmorpg.framework.rpc.spring;

import io.netty.util.internal.chmv8.ConcurrentHashMapV8;

/**
 * @author Ariescat
 * @version 2020/2/26 12:00
 */
public class RpcProviders {

	private static ConcurrentHashMapV8<String, Object> providerCache = new ConcurrentHashMapV8<>();

	static void addProvider(String className, Object bean) {
		providerCache.put(className, bean);
	}

	public static Object getProvider(String className) {
		return providerCache.get(className);
	}
}
