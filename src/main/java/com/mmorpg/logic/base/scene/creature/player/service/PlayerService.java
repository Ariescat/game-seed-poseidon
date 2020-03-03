package com.mmorpg.logic.base.scene.creature.player.service;

import com.mmorpg.logic.base.scene.creature.player.Player;
import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/3/3 11:10
 */
@Component
public class PlayerService {

	public Player getPlayerOrCreate(long playerId) {
		// TODO 查缓存 => 没有 => insert 持久化
		return null;
	}
}
