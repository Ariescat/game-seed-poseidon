package com.mmorpg.logic.base.scene;

import com.mmorpg.logic.base.scene.creature.player.Player;
import com.mmorpg.logic.base.scene.point.Point;

/**
 * @author Ariescat
 * @version 2020/3/3 14:40
 */
public class RequestEnterScene implements ISceneMessage {

	private final Player player;
	private final int sceneId;
	private final Point point;

	public RequestEnterScene(Player player, int sceneId, Point point) {
		this.player = player;
		this.sceneId = sceneId;
		this.point = point;
	}

	@Override
	public void execute(Scene curScene) {
		// ...

		curScene.beforeObjectEnterScene(player);
		curScene.objectEnterScene(player);
		curScene.afterObjectEnterScene(player);

		// ...
	}

	@Override
	public String toString() {
		return "RequestEnterScene{" +
			"player=" + player +
			", sceneId=" + sceneId +
			", point=" + point +
			'}';
	}
}
