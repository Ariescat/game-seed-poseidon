package com.mmorpg.framework.cross.server;

import com.mmorpg.framework.cross.ServerInfo;
import com.mmorpg.framework.net.NullChannel;
import io.netty.channel.Channel;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.springframework.stereotype.Component;

/**
 * 跨服 server端 Channel管理类
 *
 * @author Ariescat
 * @version 2020/2/28 11:38
 */
@Component
public class CrossChannelManager {

	private ConcurrentHashMapV8<String, Channel> channels = new ConcurrentHashMapV8<>();

	public Channel getCrossChannel(String platform, int serverId) {
		String key = ServerInfo.generateKey(platform, serverId);
		Channel channel = channels.get(key);
		if (channel == null) {
			return new NullChannel();
		} else if (channel.isActive()) {
			return channel;
		} else {
			return new NullChannel();
		}
	}

	public void putCrossChannel(String platform, int serverId, final Channel channel) {
		// TODO
		String key = ServerInfo.generateKey(platform, serverId);
		channels.compute(key, new ConcurrentHashMapV8.BiFun<String, Channel, Channel>() {
			@Override
			public Channel apply(String s, Channel old) {
				if (old == null) {
					return channel;
				}
				return null;
			}
		});
	}
}
