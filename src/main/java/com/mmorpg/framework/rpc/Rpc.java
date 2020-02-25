package com.mmorpg.framework.rpc;

import com.mmorpg.framework.cross.CrossClient;
import com.mmorpg.framework.cross.RemoteServers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ariescat
 * @version 2019/12/24 10:21
 */
public class Rpc {

	private final static Logger log = LoggerFactory.getLogger(Rpc.class);

	/**
	 * @param maxWaiteTimeMill 最长等待时间 超过这个时间的数据都强制被 timeOut
	 */
	public static RpcCallResponseFuture asyncCall(CrossClient client, RpcRequest request, long maxWaiteTimeMill) {
		if (null == client) {
			return null;
		} else {
			final RpcCallResponseFuture rpcReposeFuture = RpcFutures.newFuture(maxWaiteTimeMill, request);
			oneWayCall(client, request, rpcReposeFuture.getRequestID());
			return rpcReposeFuture;
		}
	}

	public static RpcCallResponseFuture asyncCallMethod(CrossClient client, int methodUid, Object... args) {
		return asyncCall(client,
			new RpcMethodInvokeRequest()
				.setArgs(args)
				.setMethodUid(methodUid),
			2000L);
	}

	public static void oneWayCallMethod(CrossClient client, int methodUid, Object... args) {
		oneWayCall(client,
			new RpcMethodInvokeRequest()
				.setArgs(args)
				.setMethodUid(methodUid)
		);
	}

	public static void oneWayCall(CrossClient client, RpcRequest request) {
		oneWayCall(client, request, -1);
	}

	private static void oneWayCall(CrossClient client, RpcRequest request, long reqID) {
		log.debug("RPC Call reqID:[{}], client[{}], request:{}", reqID, client, request);
		if (null != client) {
			RemoteServers.sendCrossMsg(client, request);
		}
	}

}
