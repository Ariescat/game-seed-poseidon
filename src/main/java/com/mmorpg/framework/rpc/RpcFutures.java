package com.mmorpg.framework.rpc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.mmorpg.framework.utils.Timer;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ariescat
 * @version 2019/12/24 10:46
 */
public class RpcFutures {

	// 定时检测timeOut
	private static HashedWheelTimer futureTimeOutTimer;

	private static final Map<Long, RpcCallResponseFuture> REQUEST_MAP = Maps.newConcurrentMap();
	private static final AtomicLong REQUEST_ID_IDX = new AtomicLong(1);

	private static Cache<Long, RpcRequest> REQUEST_CACHE = CacheBuilder.newBuilder()
		.expireAfterWrite(1, TimeUnit.MINUTES)
		.build();

	private static Timer REQUEST_CACHE_CLEAN_UP_TINER = new Timer(0, 1, TimeUnit.MINUTES);

	static {
		futureTimeOutTimer = new HashedWheelTimer();
		futureTimeOutTimer.start();
	}

	static Executor currentThreadExecutor = new Executor() {
		@Override
		public void execute(Runnable command) {
			command.run();
		}
	};

	public static <T> RpcResponseFuture<T> warp(T t) {
		return new SimpleResponseFuture<>(t);
	}

	public static <T> SettableResponseFuture<T> newSettableFuture() {
		return new SettableResponseFuture<>();
	}

	private static final RpcResponseFuture<?> noResponse = warp(null);

	/**
	 * rpc 调用 不返回给调用方任何消息
	 * 调用方走timeOut
	 */
	public static <T> RpcResponseFuture<T> noResponse() {
		//noinspection unchecked
		return (RpcResponseFuture<T>) noResponse;
	}

	public static RpcCallResponseFuture newFuture(long maxCacheTimeMill, RpcRequest request) {
		final long id = REQUEST_ID_IDX.incrementAndGet();
		if (request != null) {
			request.setRequestID(id);
			REQUEST_CACHE.put(id, request);
		}
		final RpcCallResponseFuture future = new RpcCallResponseFuture(id);
		future.addListener(new RpcListener() {
			@Override
			public void onRet(Object ret) {
				getAndRemoveReq(future.getRequestID());
				// 延迟清理
				if (!(ret instanceof TimeoutException)) {
					REQUEST_CACHE.invalidate(future.getRequestID());
				}
				if (REQUEST_CACHE_CLEAN_UP_TINER.isTimeOut(System.currentTimeMillis())) {
					REQUEST_CACHE.cleanUp();
				}
			}
		});

		REQUEST_MAP.put(id, future);
		futureTimeOutTimer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				timeout(future.getRequestID());
			}
		}, maxCacheTimeMill, TimeUnit.MILLISECONDS);
		return future;
	}
}
