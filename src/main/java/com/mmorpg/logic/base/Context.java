package com.mmorpg.logic.base;

import com.mmorpg.framework.asyncdb.AsyncDBService;
import com.mmorpg.framework.cross.center.CenterServerManager;
import com.mmorpg.framework.cross.client.CrossClientManager;
import com.mmorpg.framework.cross.server.CrossChannelManager;
import com.mmorpg.framework.event.EventBus;
import com.mmorpg.logic.base.scene.SceneService;
import com.mmorpg.logic.base.scene.creature.player.service.PlayerService;
import com.mmorpg.logic.base.service.ConfigService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Component
public class Context implements ApplicationContextAware {

	private static Context it;

	/**
	 * Spring 应用上下文
	 */
	private static ApplicationContext applicationContext;


	/* ============================ 系统功能 ============================ */
	/**
	 * 系统配置
	 */
	@Autowired
	public ConfigService configService;
	/**
	 * db相关
	 */
	@Autowired
	public AsyncDBService asyncDBService;
	/**
	 * 事件系统
	 */
	@Autowired
	public EventBus eventBus;
	/**
	 * 跨服相关
	 */
	@Autowired
	public CrossClientManager crossClientManager;
	@Autowired
	public CrossChannelManager crossChannelManager;
	@Autowired
	public CenterServerManager centerServerManager;


	/* ============================ 其他 ============================ */
	@Autowired
	public PlayerService playerService;
	@Autowired
	public SceneService sceneService;

//	@Autowired
//	public IActivityService activityService;

	@PostConstruct
	public void init() {
		it = this;
	}

	public static Context it() {
		return it;
	}

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

}
