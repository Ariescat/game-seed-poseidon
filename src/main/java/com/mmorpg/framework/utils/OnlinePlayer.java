package com.mmorpg.framework.utils;

import com.mmorpg.framework.net.session.CloseCause;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.net.session.GameSessionStatusUpdateCause;
import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.login.packet.RespLoginConflictPacket;
import com.mmorpg.logic.base.scene.creature.player.Player;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ariescat
 * @version 2020/2/19 11:55
 */
@Slf4j
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
	private final ConcurrentHashMap<String, List<Long>> account_2_playerIds = new ConcurrentHashMap<>();
	/**
	 * key => 玩家ID
	 */
	private final ConcurrentHashMap<String, Long> key_2_playerIds = new ConcurrentHashMap<>();
	/**
	 * 玩家昵称 => 玩家ID
	 */
	private final ConcurrentHashMap<String, Long> name_2_playerIds = new ConcurrentHashMap<>();
	/**
	 * 同IP登录数量
	 */
	private final ConcurrentHashMap<String, AtomicInteger> ip_2_count = new ConcurrentHashMap<>();

	/**
	 * 机器人ID => 会话
	 */
	private final ConcurrentHashMap<Long, GameSession> robotId_2_session = new ConcurrentHashMap<>();


	private OnlinePlayer() {
	}

	public boolean registerSession(Player player, GameSession session) {
		String key = session.getAccount_server();
		Long oldId = key_2_playerIds.putIfAbsent(key, player.getId());
		if (oldId != null) {
			GameSession oldSession = playerId_2_session.get(oldId);
			if (oldSession != null) {
				if (oldSession.setExiting()) {
					try {
						RespLoginConflictPacket packet = PacketFactory.createPacket(PacketId.RESP_LOGIN_CONFLICT);
						packet.duplicateLogin();
						packet.write();
						oldSession.getChannel().writeAndFlush(packet).addListener(ChannelFutureListener.CLOSE);
					} catch (Exception e) {
						ExceptionUtils.error(e);
					} finally {
						oldSession.close(CloseCause.Duplicate_Login, session.getIp());
					}
				}
			}
			return false;
		}

		session.registered();
		session.setPlayer(player, GameSessionStatusUpdateCause.RegisterSession);
		playerId_2_session.put(player.getId(), session);
		name_2_playerIds.put(player.getName(), player.getId());
		addPlayerId(player.getAccount(), player.getId());

		log.info("{} {} register", session.getAccount(), player.getId());
		return true;
	}

	public void removeSession(GameSession session, GameSessionStatusUpdateCause cause) {

	}

	public GameSession getSessionById(long playerId) {
		GameSession gameSession = playerId_2_session.get(playerId);
		if (gameSession == null) {
			gameSession = robotId_2_session.get(playerId);
		}
		return gameSession;
	}

	public Player getPlayerById(long playerId) {
		GameSession session = getSessionById(playerId);
		if (session != null) {
			return session.getPlayer();
		}
		return null;
	}

	public int getOnlinePlayerCount() {
		return playerId_2_session.size();
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
		// TODO
	}

	private List<Long> getPlayerIdsByAccount(String account) {
		List<Long> ids = account_2_playerIds.get(account);
		if (ids == null) {
			ids = ConcurrentUtils.putIfAbsent(account_2_playerIds, account, new LinkedList<Long>());
		}
		return ids;
	}

	private boolean removePlayerId(String account, Long playerId) {
		List<Long> ids = getPlayerIdsByAccount(account);
		synchronized (ids) {
			return ids.remove(playerId);
		}
	}

	private boolean addPlayerId(String account, Long playerId) {
		List<Long> ids = getPlayerIdsByAccount(account);
		synchronized (ids) {
			return ids.add(playerId);
		}
	}
}
