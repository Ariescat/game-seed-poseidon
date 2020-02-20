package com.mmorpg.framework.auth.impl;

import com.mmorpg.framework.auth.IKeyAuth;
import com.mmorpg.logic.base.utils.BitUtils;
import com.mmorpg.logic.base.utils.random.RandomUtils;

/**
 * @author Ariescat
 * @version 2020/2/20 15:07
 */
public class KeyAuth1 implements IKeyAuth {

	@Override
	public int genKey() {
		int key = 0;
		int salt = RandomUtils.generateBetween(17, 126);
		salt &= 0xFF;
		for (int i = 1; i < 3; i++) {
			key |= (RandomUtils.nextInt(128) << 8 * i);
		}
		key |= salt;
		key = BitUtils.cyclicLeftShift(key, 5);
		return key;
	}

	@Override
	public int restoreSalt(int key) {
		key = BitUtils.cyclicLeftShift(key, 27);
		return key & 0xFF;
	}

}
