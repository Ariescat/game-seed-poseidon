package com.mmorpg.logic.base.scene.packet;

import com.google.common.base.Preconditions;
import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.net.session.GameSessionStatus;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.RequestPacket;
import com.mmorpg.framework.packet.anno.Packet;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.scene.ISceneMessage;
import com.mmorpg.logic.base.scene.Scene;
import com.mmorpg.logic.base.scene.creature.player.Player;
import com.mmorpg.logic.base.scene.point.Point;
import lombok.extern.slf4j.Slf4j;

/**
 * 该协议处理过后，玩家才会被加入某个具体的场景对象中，并且玩家消息处理进入场景循环
 *
 * @author Ariescat
 * @version 2020/3/3 11:21
 */
@Slf4j
@Packet(commandId = PacketId.REQ_CONFIRM_ENTER_SCENE, cross = true)
public class ReqConfirmEnterScene extends RequestPacket {

	@Override
	public void read(Request request) throws Exception {

	}

	/**
	 * 第一次进场景时才会执行
	 */
	@Override
	public PacketExE executeBeforeLogin(GameSession session) {
		Player player = session.getPlayer();
		Preconditions.checkNotNull(player, "session %s", session.getAccount());

		if (session.compareAndSetStatus(GameSessionStatus.LOGIN_AUTH, GameSessionStatus.ENTERING_SCENE)) {
			// TODO 如果在任务或副本 重新发送 任务或副本的sceneInfo
			Context.it().sceneService.enterScene(player.getSceneId(), player, player.getCoord());
		}
		return null;
	}

	/**
	 * 此后场景切换执行
	 */
	@Override
	public PacketExE execute(final Player player) {
		int prepareSceneId = player.getPrepareSceneId();
		Point preparePoint = player.getPreparePoint();
		if (prepareSceneId > 0 && preparePoint != null) {
			player.getScene().addSceneMessage(new ISceneMessage() {
				@Override
				public void execute(Scene oldScene) {
					player.exitScene();
					Context.it().sceneService.enterScene(player.getSceneId(), player, player.getCoord());
				}
			});
		}
		return null;
	}
}
