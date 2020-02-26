package com.mmorpg.framework.rpc.example.method.server;

import com.mmorpg.framework.rpc.anno.RpcProvider;
import com.mmorpg.framework.rpc.example.method.base.IRpcTest;
import com.mmorpg.framework.rpc.future.RpcFutures;
import com.mmorpg.framework.rpc.future.RpcResponseFuture;
import com.mmorpg.framework.rpc.future.SettableResponseFuture;
import com.mmorpg.framework.server.ICloseEvent;
import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/26 13:00
 */
@Component
@RpcProvider(clazz = IRpcTest.class)
public class RpcServerTestImpl implements IRpcTest, ICloseEvent {

	@Override
	public RpcResponseFuture<Boolean> testNotice(long playerId, String data) {
		final SettableResponseFuture<Boolean> future = RpcFutures.newSettableFuture();
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.err.println("start new thread");
				future.set(true);
			}
		}).start();
		return future;
	}

	@Override
	public void onServerClose() {
		System.err.println("onServerClose");
	}
}
