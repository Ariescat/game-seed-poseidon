package com.mmorpg.framework.cross.client;

import com.mmorpg.framework.cross.ServerInfo;
import com.mmorpg.logic.base.scene.creature.player.Player;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 跨服 client端 CrossClient管理类
 *
 * @author Ariescat
 * @version 2020/2/25 17:36
 */
@Component
public class CrossClientManager {

	private final static Logger log = LoggerFactory.getLogger(CrossClientManager.class);

	private ConcurrentHashMapV8<String, CrossClient> clientCache = new ConcurrentHashMapV8<>();

	public CrossClient getCrossClientByServerInfo(ServerInfo serverInfo) {
		// TODO
		return null;
	}

	public void crossExit(Player player) {

	}
}
