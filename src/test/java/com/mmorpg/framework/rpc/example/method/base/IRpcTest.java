package com.mmorpg.framework.rpc.example.method.base;

import com.mmorpg.framework.rpc.anno.RpcMethod;
import com.mmorpg.framework.rpc.future.RpcResponseFuture;
import com.mmorpg.framework.rpc.proxy.RpcMethodId;

/**
 * @author Ariescat
 * @version 2020/2/26 11:59
 */
public interface IRpcTest {

	@RpcMethod(RpcMethodId.REQ_NOTICE_TEST)
	RpcResponseFuture<Boolean> testNotice(long playerId, String data);
}
