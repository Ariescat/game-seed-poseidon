package com.mmorpg.framework.auth.impl;

import com.mmorpg.framework.auth.ISNOAuth;
import com.mmorpg.logic.base.utils.random.RandomUtils;

/**
 * @author Ariescat
 * @version 2020/2/20 15:07
 */
public class SNOAuth1 implements ISNOAuth {

    @Override
    public int genSNO(int inc, int salt) {
        int rs = RandomUtils.nextInt(32767);
        int rst = (rs << 16);
        salt &= 0xFF;
        inc = rst;
        inc ^= (~rs) & 0xFF00;
        inc ^= (rs & 0xFF);
        inc ^= salt;
        return inc;
    }

    @Override
    public int restoreSNO(int sno, int salt) {
        int rs = sno >> 16;
        salt &= 0xFF;
        sno &= 0xFFFF;
        sno ^= salt;
        sno ^= (rs & 0xFF);
        sno ^= (~rs) & 0xFF00;
        return sno;
    }

}
