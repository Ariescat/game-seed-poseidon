package com.mmorpg.logic.base.utils;

import com.mmorpg.logic.base.domain.Player;
import com.mmorpg.logic.base.login.GameSession;
import com.mmorpg.logic.base.login.GameSessionStatusUpdateCause;

/**
 * @author Ariescat
 * @version 2020/2/19 11:55
 */
public class OnlinePlayer {

    private final static OnlinePlayer instance = new OnlinePlayer();

    public static OnlinePlayer getInstance() {
        return instance;
    }

    private OnlinePlayer() {
    }

    public boolean registerSession(Player player, GameSession session) {
        return false;
    }

    public void removeSession(GameSession session, GameSessionStatusUpdateCause cause) {

    }

    public int getOnlinePlayerCount() {
        return 0;
    }

    public boolean isSameIPMax(String ip) {
        return false;
    }

    public void timeoutReset(Player player, GameSessionStatusUpdateCause cause) {

    }
}
