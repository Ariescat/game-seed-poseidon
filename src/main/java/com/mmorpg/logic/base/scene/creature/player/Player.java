package com.mmorpg.logic.base.scene.creature.player;

import com.mmorpg.framework.cross.client.CrossInfo;
import com.mmorpg.framework.net.session.CloseCause;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.net.session.GameSessionStatusUpdateCause;
import com.mmorpg.framework.packet.AbstractPacket;
import com.mmorpg.framework.utils.ExceptionUtils;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.scene.creature.GObject;
import com.mmorpg.logic.base.scene.creature.player.entity.PlayerEntity;
import com.mmorpg.logic.base.scene.point.Point;
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

	/**
	 * 准备要转换的场景ID
	 */
	@Getter
	@Setter
	private int prepareSceneId;
	/**
	 * 准备要转换的传送目标点
	 */
	@Getter
	@Setter
	private Point preparePoint;
	@Getter
	@Setter
	private CrossInfo crossInfo;

	/**
	 * 玩家消息队列
	 */
	private ConcurrentLinkedQueue<IPlayerMessage> messageQueue = new ConcurrentLinkedQueue<>();
	private AtomicBoolean playerMessageEnable = new AtomicBoolean(false);

	/**
	 * 协议消息队列
	 */
	private ConcurrentLinkedQueue<AbstractPacket> packetQueue = new ConcurrentLinkedQueue<>();

	public void login() {

	}

	public void logout() {

	}

	public void setSession(GameSession gameSession, GameSessionStatusUpdateCause cause) {
		this.gameSession = gameSession;
	}

	public void closeSession(CloseCause closeCause) {

	}

	public boolean addPlayerMessage(IPlayerMessage message) {
		if (playerMessageEnable.get()) {
			return this.messageQueue.add(message);
		}
		log.error("addPlayerMessage error. {} {}", id, playerMessageEnable);
		return false;
	}

	public void addPacket(AbstractPacket packet) {
		if (gameSession != null) {
			int size = packetQueue.size();
			if (Context.it().configService.isPacketSizeToClose(size)) {
				log.warn("packetQueue too long!! {} {} {} {}", id, getAccount(), getIP(), getName());
				return;
			}
			packetQueue.add(packet);
		} else {
			log.error("have not auth yet. {} {} {} {}", id, getAccount(), getIP(), getName());
		}
	}

	public void sendPacket(AbstractPacket packet) {
		try {
			if (gameSession != null) {
				gameSession.sendPacket(packet);
			}
		} catch (Exception e) {
			ExceptionUtils.error(e);
		}
	}

	public String getAccount() {
		return playerEntity.getAccount();
	}

	public String getName() {
		return playerEntity.getName();
	}

	public String getIP() {
		return gameSession.getIp();
	}

	public boolean isCrossed() {
		return crossInfo.isCrossed();
	}

	public void exitScene() {
		// TODO
	}
}
