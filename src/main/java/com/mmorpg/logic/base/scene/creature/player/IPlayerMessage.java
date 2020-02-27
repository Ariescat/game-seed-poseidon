package com.mmorpg.logic.base.scene.creature.player;

import com.mmorpg.logic.base.scene.IMessage;

/**
 * 玩家消息
 *
 * @author Ariescat
 * @version 2020/2/27 14:32
 */
public interface IPlayerMessage extends IMessage {

	void execute(Player cPlayer);
}
