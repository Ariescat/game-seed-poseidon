package com.mmorpg.logic.base.gate;

import com.mmorpg.framework.threading.Sprite;
import com.mmorpg.framework.threading.policy.IRequestOverflowPolicy;

/**
 * 门卫
 * 处理玩家登录和注册的服务
 */
public class GateKeepers extends Sprite {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public IRequestOverflowPolicy getOverflowPolicy() {
        return null;
    }

    @Override
    public int getMaxRequestNum() {
        return 8000;
    }

    @Override
    public int getMinSleepMills() {
        return 10;
    }

    @Override
    public int getProcessPeriod() {
        return 0;
    }

    @Override
    public int getProcessNumPerPeriod() {
        return 100;
    }
}
