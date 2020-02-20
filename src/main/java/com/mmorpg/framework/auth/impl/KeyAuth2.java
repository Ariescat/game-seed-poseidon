package com.mmorpg.framework.auth.impl;

import com.mmorpg.framework.auth.IKeyAuth;
import com.mmorpg.logic.base.utils.random.RandomUtils;

/**
 * @author Ariescat
 * @version 2020/2/20 15:07
 */
public class KeyAuth2 implements IKeyAuth {

    @Override
    public int genKey() {
        int key = 0;
        int salt = RandomUtils.generateBetween(17, 120);
        key ^= RandomUtils.generateBetween(10, 126) << 8;
        key ^= RandomUtils.generateBetween(10, 126) << 16;
        key ^= RandomUtils.generateBetween(10, 126) << 24;
        key |= salt;
        key = key << 13 | key >>> 19;
        key += 5;
        return key;
    }

    @Override
    public int restoreSalt(int key) {
        key = key << 19 | key >> 13;
        key ^= key >> 24;
        key ^= key >> 16;
        key ^= key >> 8;
        key -= 5;
        return key;
    }

}
