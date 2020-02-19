package com.mmorpg.logic.base.login;

/**
 * @author Ariescat
 * @version 2020/2/19 11:46
 */
public enum GameSessionStatus {
    /**
     * 初始
     */
    INIT,

    /**
     * 已验证
     */
    LOGIN_AUTH,

    /**
     * 正在进入场景(还没初始化玩家数据)
     */
    ENTERING_SCENE,

    /**
     * 已经进入场景(已经初始化玩家数据)
     */
    ENTERED_SCENE,

    /**
     * 退出中
     */
    LOGOUTING,

}
