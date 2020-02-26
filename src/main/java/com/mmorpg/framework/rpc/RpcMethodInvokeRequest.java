package com.mmorpg.framework.rpc;

import com.koloboke.function.Consumer;
import com.mmorpg.framework.rpc.future.RpcFutures;
import com.mmorpg.framework.rpc.future.RpcListener;
import com.mmorpg.framework.rpc.future.RpcResponseFuture;
import com.mmorpg.framework.rpc.future.SimpleResponseFuture;
import com.mmorpg.framework.rpc.msg.CrossMsgId;
import com.mmorpg.framework.rpc.msg.anno.CrossMsg;
import com.mmorpg.framework.rpc.proxy.MethodCache;
import com.mmorpg.framework.rpc.proxy.RpcMethodId;
import com.mmorpg.framework.rpc.spring.RpcProviders;
import com.mmorpg.logic.base.Context;
import org.apache.commons.collections.MapUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ariescat
 * @version 2020/2/23 10:19
 */
@CrossMsg(CrossMsgId.RPC_METHOD_INVOKE_REQUEST)
public class RpcMethodInvokeRequest extends RpcRequest {

	/**
	 * 提供一个临时修复数据的获取接口
	 */
	private static final ThreadLocal<Map<String, Object>> repair_msg_local = new ThreadLocal<>();

	private int methodUid;

	private String clazzName;
	private String methodName;
	private String methodDesc;

	private Object[] args;

	public RpcMethodInvokeRequest(int methodUid, Object[] args) {
		this.methodUid = methodUid;
		this.args = args;
	}

	public RpcMethodInvokeRequest(String clazzName, String methodName, String methodDesc, Object[] args) {
		this.clazzName = clazzName;
		this.methodName = methodName;
		this.methodDesc = methodDesc;
		this.args = args;
	}

	public static Map<String, Object> getRepairMsg() {
		return repair_msg_local.get();
	}

	@Override
	protected void executeRequestAsync(final Consumer<RpcResponse> consumer) {
		try {
			Method method = MethodCache.getOrFindMethod(methodUid, clazzName, methodName, methodDesc);
			if (method == null) {
				sendError(consumer, "找不到对应方法: " + methodUid + "->" + clazzName + "@" + methodName + "#" + methodDesc);
			} else {
				Class<?> clazz = method.getDeclaringClass();
				String clazzName = clazz.getName();
				Object serviceBean = RpcProviders.getProvider(clazzName);
				if (serviceBean == null) {
					try {
						serviceBean = Context.getBean(clazz);
					} catch (Exception e) {
						sendError(consumer, e);
					}
				}
				if (serviceBean == null) {
					sendError(consumer, "找不到实现类: " + clazzName + "@" + method);
					return;
				}

				// 处理 NULL
				if (args != null) {
					for (int i = 0; i < args.length; i++) {
						Object arg = args[i];
						if (arg != null && arg.getClass() == Object.class) {
							args[i] = null;
						}
					}
				}

				boolean haveRepairMsg = MapUtils.isNotEmpty(repairMap);
				if (haveRepairMsg) {
					repair_msg_local.set(repairMap);
				}
				Object invoke = method.invoke(serviceBean, args);
				if (haveRepairMsg) {
					repair_msg_local.set(null);
				}

				// 返回执行结果
				if (invoke != RpcFutures.noResponse() && !method.getReturnType().equals(Void.TYPE)) {
					if (invoke instanceof SimpleResponseFuture) {
						sendSuc(consumer, ((SimpleResponseFuture) invoke).get());
					} else if (invoke instanceof RpcResponseFuture) {
						((RpcResponseFuture) invoke).addListener(new RpcListener() {
							@Override
							public void onRet(Object ret) {
								sendSuc(consumer, ret);
							}
						});
					} else if (invoke instanceof Throwable) {
						sendError(consumer, (Throwable) invoke);
					} else {
						sendSuc(consumer, invoke);
					}
				}
			}
		} catch (Exception e) {
			sendError(consumer, e);
		}
	}

	private void sendSuc(Consumer<RpcResponse> consumer, Object ret) {
		RpcResponse response = new RpcResponse();
		response.setRet(ret);
		consumer.accept(response);
	}

	private void sendError(Consumer<RpcResponse> consumer, String error) {
		RpcResponse response = new RpcResponse();
		response.setError(error);
		consumer.accept(response);
	}

	private void sendError(Consumer<RpcResponse> consumer, Throwable t) {
		StringWriter stringWriter = new StringWriter();
		t.printStackTrace(new PrintWriter(stringWriter));
		sendError(consumer, stringWriter.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RpcMethodInvokeRequest{");
		String desc = MethodDescHolder.descMap.get(methodUid);
		if (desc != null) {
			sb.append("methodUid=").append(methodUid).append('(').append(desc).append(')');
		}
		if (clazzName != null) {
			sb.append(", className='").append(clazzName).append('\'');
		}
		if (methodName != null) {
			sb.append(", methodName='").append(methodName).append('\'');
		}
		if (methodDesc != null) {
			sb.append(", methodDesc='").append(methodDesc).append('\'');
		}
		sb.append(", args=").append(Arrays.toString(args)).append("}");
		return sb.toString();
	}

	private static class MethodDescHolder {
		static final Map<Object, String> descMap;

		static {
			descMap = new HashMap<>();
			for (Field field : RpcMethodId.class.getDeclaredFields()) {
				try {
					Integer value = (Integer) field.get(RpcMethodId.class);
					descMap.put(value, field.getName());
				} catch (Exception ignored) {
				}
			}
		}
	}
}
