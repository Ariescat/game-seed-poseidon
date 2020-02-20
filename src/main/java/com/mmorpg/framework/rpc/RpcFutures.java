package com.mmorpg.framework.rpc;

import java.util.concurrent.Executor;

/**
 * @author Ariescat
 * @version 2019/12/24 10:46
 */
public class RpcFutures {

	static Executor currentThreadExecutor = new Executor() {
		@Override
		public void execute(Runnable command) {
			command.run();
		}
	};
}
