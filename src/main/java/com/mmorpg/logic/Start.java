package com.mmorpg.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动器
 *
 * @author Ariescat
 * @version 2020/2/19 12:37
 */
public class Start {

	private final static Logger log = LoggerFactory.getLogger(Start.class);

	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
}
