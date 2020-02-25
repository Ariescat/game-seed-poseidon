package com.mmorpg.framework.rpc;

import com.koloboke.function.Consumer;

/**
 * @author Ariescat
 * @version 2020/2/23 10:19
 */
public class RpcMethodInvokeMsg extends RpcRequest {

	@Override
	protected void executeRequestAsync(Consumer<RpcResponse> consumer) {

	}

	public RpcMethodInvokeMsg setArgs(Object[] args) {
		return null;
	}

	public RpcMethodInvokeMsg setMethodUid(int methodUid) {
		return null;
	}
}
