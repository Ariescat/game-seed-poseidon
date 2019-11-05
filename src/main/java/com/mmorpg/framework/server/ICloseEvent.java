package com.mmorpg.framework.server;

public interface ICloseEvent {

    /**
     * JVM关闭时触发的事件
     */
    void onServerClose();
}
