package com.mmorpg.framework.cross;

import com.mmorpg.framework.rpc.msg.ICrossBaseMsg;
import com.mmorpg.framework.rpc.msg.ICrossPlayerMsg;
import com.mmorpg.framework.rpc.msg.packet.CrossMsgPacket;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.domain.Player;
import io.netty.channel.Channel;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author Ariescat
 * @version 2020/2/23 10:18
 */
public enum RemoteServers {

	DEVIL_ALTER,
	;

	private final static Logger log = LoggerFactory.getLogger(RemoteServers.class);
	/**
	 * platform_serverId -> serverInfo
	 */
	protected ConcurrentHashMapV8<String, ServerInfo> serverCache = new ConcurrentHashMapV8<>();

	// =========================== serverCache ===========================
	public ServerInfo getServerInfo(String platform, int serverId) {
		return serverCache.get(ServerInfo.generateKey(platform, serverId));
	}

	public void init(Collection<? extends ServerInfo> serverInfos) {
		serverCache.clear();
		addServerInfos(serverInfos);
		checkAndCloseClient();
	}

	public void addServerInfos(Collection<? extends ServerInfo> serverInfos) {
		for (ServerInfo serverInfo : serverInfos) {
			addServerInfo(serverInfo);
		}
	}

	public void addServerInfo(ServerInfo serverInfo) {
		if (serverInfo == null) {
			return;
		}
		ServerInfo old = serverCache.put(serverInfo.getKey(), serverInfo);
		if (old != null && !old.getIpPort().equals(serverInfo.getIpPort())) {
			// 连接需要更新
			checkAndCloseClient(old);
		}
	}

	private void checkAndCloseClient(ServerInfo serverInfo) {
		// TODO
	}

	/**
	 * 检查所有IP列表，并关闭没有引用的连接
	 */
	private void checkAndCloseClient() {
		// TODO
	}


	// =========================== client ===========================
	public static CrossClient getCenterServerClient() {
		ServerInfo centerServerInfo = Context.it().centerServerManager.getCenterServerInfo();
		return Context.it().crossClientManager.getCrossClientByServerInfo(centerServerInfo);
	}

	public static CrossClient getCrossClient(String ipPort) {
		return Context.it().crossClientManager.getCrossClientByServerInfo(new ServerInfo(ipPort));
	}

	public static CrossClient getCrossClient(Player player) {
		CrossInfo crossInfo = player.getCrossInfo();
		return getCrossClient(crossInfo.getCrossType(), crossInfo.getPlatform(), crossInfo.getServerId());
	}

	public static CrossClient getCrossClient(CrossType crossType, String platform, int serverId) {
		return crossType.getServers().getCrossClient(platform, serverId);
	}

	public CrossClient getCrossClient(String platform, int serverId) {
		ServerInfo serverInfo = getServerInfo(platform, serverId);
		if (serverInfo == null) {
			log.warn("ServerInfo is null, {} {} {}", name(), platform, serverId, new Exception());
			return null;
		}
		return Context.it().crossClientManager.getCrossClientByServerInfo(serverInfo);
	}


	// =========================== sendCrossMsg ===========================
	public static void sendCrossMsg(CrossClient crossClient, ICrossBaseMsg<?> msg) {
		DispatchPacket packet = createCrossMsgPacket(msg);
//		TODO
//		crossClient.sendPacket(packet);
	}

	public static void sendCrossMsg(Channel channel, ICrossBaseMsg<?> msg) {
		channel.writeAndFlush(createCrossMsgPacket(msg));
	}

	public static void sendCrossMsgToCenter(ICrossBaseMsg<?> msg) {
		sendCrossMsg(getCenterServerClient(), msg);
	}

	private static DispatchPacket createCrossMsgPacket(ICrossBaseMsg<?> msg) {
		CrossMsgPacket packet = CrossMsgPacket.of(msg);
		long playerId = 0;
		if (msg instanceof ICrossPlayerMsg) {
			playerId = ((ICrossPlayerMsg) msg).getPlayerId();
		}
		return new DispatchPacket(playerId, packet);
	}
}
