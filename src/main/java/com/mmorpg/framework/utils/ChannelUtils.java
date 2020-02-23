package com.mmorpg.framework.utils;

import com.mmorpg.framework.net.session.GameSession;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * @author Ariescat
 * @version 2020/2/19 14:40
 */
public class ChannelUtils {
	private static final AttributeKey<GameSession> SESSION_KEY = AttributeKey.valueOf("session-key");

	/**
	 * 添加会调
	 */
	public static boolean addChannelSession(Channel channel, GameSession session) {
		Attribute<GameSession> sessionAttr = channel.attr(SESSION_KEY);
		return sessionAttr.compareAndSet(null, session);
	}

	/**
	 * 获取会话
	 */
	public static GameSession getChannelSession(Channel channel) {
		Attribute<GameSession> sessionAttr = channel.attr(SESSION_KEY);
		return sessionAttr.get();
	}

	/**
	 * 移除会话
	 */
	public static void removeChannelSession(Channel channel) {
		Attribute<GameSession> sessionAttr = channel.attr(SESSION_KEY);
		if (sessionAttr != null) {
			sessionAttr.remove();
		}
	}

	/**
	 * 获得IP地址
	 */
	public static String getIP(Channel channel) {
		return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
	}

	public static String getAccount(Channel channel) {
		GameSession session = getChannelSession(channel);
		if (session != null) {
			return session.getAccount();
		}
		return "";
	}
}
