package com.mmorpg.framework.cross;

import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
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
}
