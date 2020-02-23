package com.mmorpg.logic.base.domain;

import com.mmorpg.framework.cross.CrossInfo;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.net.session.GameSessionStatusUpdateCause;

/**
 * @author Ariescat
 * @version 2020/2/19 11:54
 */
public class Player {

	private String account;

	private GameSession gameSession;
	private CrossInfo crossInfo;

	public long getId() {
		return 0;
	}

	public void logout() {

	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public GameSession getGameSession() {
		return gameSession;
	}

	public void setSession(GameSession gameSession, GameSessionStatusUpdateCause cause) {
		this.gameSession = gameSession;
	}

	public CrossInfo getCrossInfo() {
		return crossInfo;
	}

	public void setCrossInfo(CrossInfo crossInfo) {
		this.crossInfo = crossInfo;
	}

}
