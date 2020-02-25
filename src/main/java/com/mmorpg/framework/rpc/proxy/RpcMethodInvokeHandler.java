package com.mmorpg.framework.rpc.proxy;

import com.google.common.reflect.AbstractInvocationHandler;
import com.mmorpg.framework.cross.CrossClient;
import com.mmorpg.framework.cross.RemoteServers;
import com.mmorpg.framework.rpc.anno.RpcClient;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.Type;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Ariescat
 * @version 2020/2/25 16:49
 */
@SuppressWarnings("UnstableApiUsage")
public class RpcMethodInvokeHandler extends AbstractInvocationHandler {
	private final static Logger log = LoggerFactory.getLogger(RpcMethodInvokeHandler.class);

	private static final Object NULL = new Object();
	private Class<?> interfaceClass;
	private Map<Method, String> methodDesc = new ConcurrentHashMapV8<>(); // 方法描述

	public RpcMethodInvokeHandler(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	@Override
	protected Object handleInvocation(Object o, Method method, Object[] objects) throws Throwable {
		return null;
	}

	private CrossClient getCrossClient(Method method, Object[] args) {
		RpcClient client = method.getAnnotation(RpcClient.class);
		if (client != null && StringUtils.isNotEmpty(client.value())) {
			return RemoteServers.getCrossClient(client.value());
		}

		if (args != null && args.length > 0) {
			CrossClient crossClient = null;
			for (int i = 0; i < args.length; i++) {
				if (args[i] == null) {
					args[i] = NULL;
				} else if (args[i] instanceof CrossClient) {
					crossClient = (CrossClient) args[i];
					args[i] = NULL;
				}
			}
			if (crossClient != null) {
				return crossClient;
			}
		}
		return RemoteServers.getCenterServerClient();
	}

	private String getDesc(Method method) {
		String s = methodDesc.get(method);
		if (s == null) {
			s = Type.getMethodDescriptor(method);
			methodDesc.put(method, s);
		}
		return s;
	}
}
