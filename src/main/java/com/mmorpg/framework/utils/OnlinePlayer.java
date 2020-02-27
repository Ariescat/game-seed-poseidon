package com.mmorpg.framework.utils;

import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.net.session.GameSessionStatusUpdateCause;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.scene.creature.player.Player;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ariescat
 * @version 2020/2/19 11:55
 */
public class OnlinePlayer {

	private final static OnlinePlayer instance = new OnlinePlayer();

	public static OnlinePlayer getInstance() {
		return instance;
	}

	/**
	 * 玩家ID => 会话
	 */
	private final ConcurrentHashMap<Long, GameSession> playerId_2_session = new ConcurrentHashMap<>();
	/**
	 * 玩家账号 => 玩家ID
	 */
	private final ConcurrentHashMap<String, Set<Long>> account_2_playerIds = new ConcurrentHashMap<>();
	/**
	 * 玩家账号 => 玩家ID
	 */
	private final ConcurrentHashMap<String, AtomicInteger> ip_2_count = new ConcurrentHashMap<>();

	private OnlinePlayer() {
	}

	public boolean registerSession(Player player, GameSession session) {
//		player.login();
//		session.setPlayer(player, GameSessionStatusUpdateCause.RegisterSession);
		return false;
	}

	public void removeSession(GameSession session, GameSessionStatusUpdateCause cause) {

	}

	public Player getPlayerById(long playerId) {
		return null;
	}

	public int getOnlinePlayerCount() {
		return 0;
	}

	public AtomicInteger getSameIPCount(String ip) {
		AtomicInteger count = ip_2_count.get(ip);
		if (count == null) {
			count = new AtomicInteger(0);
			count = ConcurrentUtils.putIfAbsent(ip_2_count, ip, count);
		}
		return count;
	}

	public boolean isSameIPMax(String ip) {
		final int maxSameIp = Context.it().configService.getMaxSameIpCount();
		if (maxSameIp > 0) {
			if (Context.it().configService.isInSameIpWhiteList(ip)) {
				return false;
			}
			AtomicInteger count = getSameIPCount(ip);
			return count.get() > maxSameIp;
		}
		return false;
	}

	public void timeoutReset(Player player, GameSessionStatusUpdateCause cause) {

	}
}
