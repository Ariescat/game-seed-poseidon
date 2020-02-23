package com.mmorpg.framework.rpc;

import java.util.concurrent.TimeUnit;

/**
 * @author Ariescat
 * @version 2020/2/23 10:28
 */
public class SimpleResponseFuture<T> implements RpcResponseFuture<T> {
	private T ret;

	SimpleResponseFuture(T ret) {
		this.ret = ret;
	}

	@Override
	public long getRequestID() {
		return 0;
	}

	@Override
	public void addListener(RpcListener listener) {
		if (listener != null) {
			listener.onRet(ret);
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public T get() {
		return ret;
	}

	@Override
	public T get(long timeout, TimeUnit unit) {
		return ret;
	}

}
