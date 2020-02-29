package com.mmorpg;

import com.mmorpg.framework.rpc.example.method.client.RpcClientTest;
import com.mmorpg.logic.modules.activity.IActivityService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

/**
 * @author Ariescat
 * @version 2020/2/27 9:52
 */
public class TestStart {

	public static void main(String[] args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		stopWatch.stop();
		System.err.println("used time:" + stopWatch.getTotalTimeMillis());

		// test rpc
		RpcClientTest clientTest = context.getBean(RpcClientTest.class);
		clientTest.test();

		// test groovy
//		IActivityService activityService = context.getBean(IActivityService.class);
//		activityService.reqInfo();
	}
}
