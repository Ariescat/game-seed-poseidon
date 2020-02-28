package com.mmorpg.framework.rpc.proxy;

import com.google.common.reflect.AbstractInvocationHandler;
import com.mmorpg.framework.cross.client.CrossClient;
import com.mmorpg.framework.cross.RemoteServers;
import com.mmorpg.framework.rpc.Rpc;
import com.mmorpg.framework.rpc.RpcMethodInvokeRequest;
import com.mmorpg.framework.rpc.anno.RpcClient;
import com.mmorpg.framework.rpc.anno.RpcTimeout;
import com.mmorpg.framework.rpc.future.RpcCallResponseFuture;
import com.mmorpg.framework.rpc.future.RpcFutures;
import com.mmorpg.framework.rpc.future.RpcResponseFuture;
import com.mmorpg.framework.rpc.future.SettableResponseFuture;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.Type;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
	protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
		RpcCallResponseFuture rpcReposeFuture = null;
		try {
			// build rpc request
			RpcMethodInvokeRequest msg;
			Integer methodUid = MethodCache.method2uid.get(method);
			if (methodUid == null) {
				msg = new RpcMethodInvokeRequest(
					interfaceClass.getName(),
					getDesc(method),
					method.getName(),
					args);
			} else {
				msg = new RpcMethodInvokeRequest(methodUid, args);
			}

			// find cross client
			CrossClient client = getCrossClient(method, args);
			if (client == null) {
				if (method.getReturnType() == RpcResponseFuture.class) {
					SettableResponseFuture<Object> ret = RpcFutures.newSettableFuture();
					ret.setException(new IOException("[RpcMethodInvokeHandler] client is null send msg fail: " + msg));
					return ret;
				} else {
					log.error("[ RpcMethodInvokeHandler] client is null send msg fail [{}]", msg);
					return null;
				}
			}

			// rpc call
			if (method.getReturnType() == Void.TYPE) {
				//异步
				Rpc.oneWayCall(client, msg);
			} else {
				long timeOutMill = 60000L;
				RpcTimeout annotation = method.getAnnotation(RpcTimeout.class);
				if (annotation != null) {
					timeOutMill = annotation.value();
				}
				rpcReposeFuture = Rpc.asyncCall(client, msg, timeOutMill);
				if (method.getReturnType() == RpcResponseFuture.class) {
					return rpcReposeFuture;
				} else {
					return rpcReposeFuture.get(timeOutMill, TimeUnit.MILLISECONDS);
				}
			}
		} catch (Exception e) {
			if (rpcReposeFuture != null) {
				rpcReposeFuture.exception(e);
			}
			log.error("", e);
		}
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
