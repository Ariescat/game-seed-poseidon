package com.mmorpg.framework.rpc.spring;

import com.mmorpg.framework.rpc.proxy.MethodCache;
import com.mmorpg.framework.rpc.proxy.RpcMethodInvokeHandler;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * RpcConsumer 构建类
 *
 * @author Ariescat
 * @version 2020/2/26 11:23
 */
public class RpcConsumers {

	private final static Logger log = LoggerFactory.getLogger(RpcConsumers.class);

	private static ConcurrentHashMapV8<Class, Object> proxyCache = new ConcurrentHashMapV8<>();

	static Object createRpcProxy(Class<?> interfaceClass) {
		log.debug("createRpcProxy: {}", interfaceClass);

		return Proxy.newProxyInstance(interfaceClass.getClassLoader(),
			new Class<?>[]{interfaceClass},
			new RpcMethodInvokeHandler(interfaceClass));
	}

	public static <T> T get(final Class<T> type) {
		Object obj = proxyCache.get(type);
		if (obj == null) {
			MethodCache.cacheMethods(type);
			obj = proxyCache.computeIfAbsent(type, new ConcurrentHashMapV8.Fun<Class, Object>() {
				@Override
				public Object apply(Class aClass) {
					return createRpcProxy(type);
				}
			});
		}
		//noinspection unchecked
		return (T) obj;
	}

}
