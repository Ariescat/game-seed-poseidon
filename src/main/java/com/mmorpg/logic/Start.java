package com.mmorpg.logic;

import com.mmorpg.framework.http.asynchttp.AsyncHttpClientUtils;
import com.mmorpg.framework.listener.ListenerManager;
import com.mmorpg.framework.net.NetServer;
import com.mmorpg.logic.base.login.GateKeepers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

/**
 * 启动器
 *
 * @author Ariescat
 * @version 2020/2/19 12:37
 */
public class Start {

	private final static Logger log = LoggerFactory.getLogger(Start.class);

	public static void main(String[] args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ListenerManager.getInstance().init(context);
		GateKeepers.initialize();
		NetServer.startServer(4010);

		stopWatch.stop();
		log.warn("start finish used time:{}", stopWatch.getTotalTimeMillis());

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Start.stop();
			}
		});
	}

	public static void stop() {
		log.warn("stop game server");
		AsyncHttpClientUtils.stop();
	}
}
