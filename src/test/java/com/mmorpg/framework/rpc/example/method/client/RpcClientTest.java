package com.mmorpg.framework.rpc.example.method.client;

import com.mmorpg.framework.rpc.anno.RpcConsumer;
import com.mmorpg.framework.rpc.example.method.base.IRpcTest;
import com.mmorpg.framework.rpc.future.RpcListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/26 13:04
 */
@Component
@Slf4j
public class RpcClientTest {

	@RpcConsumer
	private IRpcTest rpcTest;

	public void test() {
		rpcTest.testNotice(1001L, "hello rpc").addListener(new RpcListener() {
			@Override
			public void onRet(Object ret) {
				log.info("client receive:{}", ret);
				if (ret instanceof Boolean) {
					System.err.println("ret is boolean");
				}
			}
		});
	}
}
