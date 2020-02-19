package com.mmorpg.logic.base.service;

import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/19 15:05
 */
@Component
public class ConfigService {

    public int getMaxOnlineCount() {
        return 0;
    }

    public boolean isFlushStatus() {
        return false;
    }

    public long getPlugInterval() {
        return 0;
    }

    public int getPlugThreshold() {
        return 0;
    }

    public String getOriServerFlag() {
        return null;
    }
}
