package com.mmorpg.framework.packet;

import com.mmorpg.logic.base.login.GameSession;
import com.mmorpg.logic.base.packet.Response;

/**
 * @author Ariescat
 * @version 2020/2/19 11:43
 */
public abstract class AbstractPacket {

    public abstract Short getCommand();

    public abstract void executeBeforeLogin(GameSession session);

    public abstract Response write();
}
