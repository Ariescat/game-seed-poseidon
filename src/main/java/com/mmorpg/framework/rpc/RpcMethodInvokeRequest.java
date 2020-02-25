package com.mmorpg.framework.rpc;

import com.koloboke.function.Consumer;
import com.mmorpg.framework.rpc.msg.CrossMsgId;
import com.mmorpg.framework.rpc.msg.anno.CrossMsg;

/**
 * @author Ariescat
 * @version 2020/2/23 10:19
 */
@CrossMsg(CrossMsgId.RPC_METHOD_INVOKE_REQUEST)
public class RpcMethodInvokeRequest extends RpcRequest {

	@Override
	protected void executeRequestAsync(Consumer<RpcResponse> consumer) {

	}

	public RpcMethodInvokeRequest setArgs(Object[] args) {
		return null;
	}

	public RpcMethodInvokeRequest setMethodUid(int methodUid) {
		return null;
	}
}
