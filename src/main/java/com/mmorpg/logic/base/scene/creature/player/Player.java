package com.mmorpg.logic.base.scene.creature.player;

import com.mmorpg.framework.cross.CrossInfo;
import com.mmorpg.framework.net.session.CloseCause;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.net.session.GameSessionStatusUpdateCause;
import com.mmorpg.framework.packet.AbstractPacket;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.scene.creature.GObject;
import com.mmorpg.logic.base.scene.creature.player.entity.PlayerEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ariescat
 * @version 2020/2/19 11:54
 */
public class Player extends GObject {

	/**
	 * 玩家实体
	 */
	@Getter
	@Setter
	private PlayerEntity playerEntity;

	@Getter
	private GameSession gameSession;

	@Getter
	@Setter
	private CrossInfo crossInfo;

	/**
	 * 玩家消息队列
	 */
	private ConcurrentLinkedQueue<IPlayerMessage> playerMessages = new ConcurrentLinkedQueue<>();
	private AtomicBoolean playerMessageEnable = new AtomicBoolean(false);

	/**
	 * 协议消息队列
	 */
	private ConcurrentLinkedQueue<AbstractPacket> packetQueue = new ConcurrentLinkedQueue<>();

	public void addPacket(AbstractPacket packet) {
		if (gameSession != null) {
			int size = packetQueue.size();
			if (Context.it().configService.isPacketSizeToClose(size)) {
				log.warn("packetQueue too long!! {} {} {}", getAccount(), getIP(), getName());
				return;
			}
			packetQueue.add(packet);
		} else {
			log.error("[{}], have not auth yet.", id);
		}
	}

	public void logout() {

	}

	public String getAccount() {
		return playerEntity.getAccount();
	}

	public String getName() {
		return playerEntity.getName();
	}

	public String getIP() {
		return gameSession.getIP();
	}

	public void setSession(GameSession gameSession, GameSessionStatusUpdateCause cause) {
		this.gameSession = gameSession;
	}

	public void closeSession(CloseCause closeCause) {

	}
}
