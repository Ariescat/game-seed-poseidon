package com.mmorpg.framework.rpc;

import java.util.concurrent.Future;

/**
 * @author Ariescat
 * @version 2019/12/24 10:22
 */
public interface RpcResponseFuture<T> extends Future<T> {

	long getRequestID();

	void addListener(RpcListener listener);
}
