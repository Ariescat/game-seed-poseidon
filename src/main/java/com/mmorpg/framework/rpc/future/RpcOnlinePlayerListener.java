package com.mmorpg.framework.rpc.future;

import com.mmorpg.framework.utils.OnlinePlayer;
import com.mmorpg.logic.base.scene.creature.player.IPlayerMessage;
import com.mmorpg.logic.base.scene.creature.player.Player;

/**
 * 指定在线玩家队列执行
 *
 * @author Ariescat
 * @version 2020/2/26 12:17
 */
public abstract class RpcOnlinePlayerListener implements RpcListener {

	private final long playerId;

	public RpcOnlinePlayerListener(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public void onRet(final Object ret) {
		Player player = OnlinePlayer.getInstance().getPlayerById(playerId);
		if (player != null) {
			player.addPlayerMessage(new IPlayerMessage() {
				@Override
				public void execute(Player cPlayer) {
					onRet(cPlayer, ret);
				}
			});
		} else {
			onRet(null, ret);
		}
	}

	public abstract void onRet(Player player, Object ret);
}
