package com.mmorpg.framework;

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

//		IActivityService activityService = context.getBean(IActivityService.class);
//		activityService.reqInfo();
	}
}
