package com.mmorpg.logic.base.login;

import com.mmorpg.framework.packet.AbstractPacket;
import com.mmorpg.framework.threading.IRequestToGod;

/**
 * 请求进入游戏世界的签证
 */
public class RequestEnterWorld implements IRequestToGod {

    private final AbstractPacket packet;
    private final GameSession session;

    public RequestEnterWorld(final GameSession session, final AbstractPacket packet) {
        this.session = session;
        this.packet = packet;
    }

    public boolean execute() {
        packet.executeBeforeLogin(session);
        return true;
    }

    @Override
    public String toString() {
        return "RequestEnterWorld[packet = " + packet + "]";
    }

}
