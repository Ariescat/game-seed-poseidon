package com.mmorpg.logic.base;

import com.mmorpg.framework.asyncdb.AsyncDBService;
import com.mmorpg.framework.event.EventBus;
import com.mmorpg.logic.base.service.ConfigService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

public class Context implements ApplicationContextAware {

	/**
	 * Spring 应用上下文
	 */
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	public static <T> Collection<T> getBeansOfType(Class<T> clazz) {
		return applicationContext.getBeansOfType(clazz).values();
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	/*
	 * ========================= 注入Spring组件 =========================
	 */
	private static AsyncDBService asyncDBService;
	private static ConfigService configService;
	private static EventBus eventBus;

	public static AsyncDBService getAsyncDBService() {
		return asyncDBService;
	}

	@Autowired
	public void setAsyncDBService(AsyncDBService asyncDBService) {
		Context.asyncDBService = asyncDBService;
	}

	public static ConfigService getConfigService() {
		return configService;
	}

	@Autowired
	public void setConfigService(ConfigService configService) {
		Context.configService = configService;
	}

	public static EventBus getEventBus() {
		return eventBus;
	}

	@Autowired
	public void setEventBus(EventBus eventBus) {
		Context.eventBus = eventBus;
	}
}
