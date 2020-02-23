package com.mmorpg.logic.base.login;

import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.net.session.GameSessionStatus;
import com.mmorpg.framework.net.session.GameSessionStatusUpdateCause;
import com.mmorpg.framework.thread.god.IRequestToGod;
import com.mmorpg.framework.utils.OnlinePlayer;
import com.mmorpg.logic.base.domain.Player;

/**
 * @author Ariescat
 * @version 2020/2/19 11:45
 */
public class RequestExitWorld implements IRequestToGod {
	private final GameSession session;

	public RequestExitWorld(GameSession session) {
		this.session = session;
	}

	public boolean isNeedLogoutMessage(GameSessionStatus status) {
		return status == GameSessionStatus.ENTERING_SCENE
			|| status == GameSessionStatus.ENTERED_SCENE;
	}

	@Override
	public boolean execute() {
		while (true) {
			GameSessionStatus status = session.getStatus();
			if (status == GameSessionStatus.LOGOUTING) {
				break;
			}
			if (session.compareAndSetStatus(status, GameSessionStatus.LOGOUTING)) {
				if (this.isNeedLogoutMessage(status)) {
					Player player = session.getPlayer();
					player.logout();
				} else {
					OnlinePlayer.getInstance().removeSession(session, GameSessionStatusUpdateCause.RequestExitWorld);
				}
				break;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "RequestExitWorld [session=" + session + " ]";
	}
}

