package com.mmorpg.framework.rpc;

import com.google.common.util.concurrent.AbstractFuture;

/**
 * @author Ariescat
 * @version 2020/2/23 10:31
 */
public class SettableResponseFuture<T> extends AbstractFuture<T> implements RpcResponseFuture<T> {

	@Override
	public long getRequestID() {
		return 0;
	}

	@Override
	public void addListener(final RpcListener listener) {
		super.addListener(new Runnable() {
			@Override
			public void run() {
				Object o;
				try {
					o = get();
				} catch (Throwable e) {
					o = e;
				}
				listener.onRet(o);
			}
		}, RpcFutures.currentThreadExecutor);
	}

	@Override
	public boolean set(T value) {
		return super.set(value);
	}

	@Override
	public boolean setException(Throwable throwable) {
		return super.setException(throwable);
	}
}
