package com.mmorpg.framework.rpc;

import com.koloboke.function.Consumer;
import com.mmorpg.framework.rpc.msg.CrossMsgId;
import com.mmorpg.framework.rpc.msg.anno.CrossMsg;
import com.mmorpg.framework.rpc.proxy.RpcMethodId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ariescat
 * @version 2020/2/23 10:19
 */
@CrossMsg(CrossMsgId.RPC_METHOD_INVOKE_REQUEST)
public class RpcMethodInvokeRequest extends RpcRequest {

	private static final ThreadLocal<Map<String, Object>> repair_msg_local = new ThreadLocal<>();

	private int methodUid;

	private String clazzName;
	private String methodName;
	private String methodDesc;

	private Object[] args;

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

	@Override
	protected void executeRequestAsync(Consumer<RpcResponse> consumer) {

	}

}
