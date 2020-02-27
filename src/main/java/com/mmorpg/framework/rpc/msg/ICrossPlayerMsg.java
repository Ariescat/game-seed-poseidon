package com.mmorpg.framework.rpc.msg;

import com.mmorpg.framework.rpc.msg.repair.TempRepairMsg;
import com.mmorpg.logic.base.scene.creature.player.Player;

/**
 * @author Ariescat
 * @version 2020/2/23 11:21
 */
public abstract class ICrossPlayerMsg extends TempRepairMsg implements ICrossBaseMsg<Player> {

	/**
	 * 此处返回的玩家ID必须大于0
	 * 如果确定为非玩家消总请用 {@link ICrossMsg}
	 */
	public abstract long getPlayerId();

}
