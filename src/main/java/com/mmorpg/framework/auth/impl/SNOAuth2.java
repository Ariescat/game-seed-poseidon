package com.mmorpg.framework.auth.impl;

import com.mmorpg.framework.auth.ISNOAuth;
import com.mmorpg.framework.utils.random.RandomUtils;

/**
 * @author Ariescat
 * @version 2020/2/20 16:53
 */
public class SNOAuth2 implements ISNOAuth {

	@Override
	public int genSNO(int inc, int salt) {
		int rs = RandomUtils.nextInt(32767);
		int rst = (rs ^ salt) << 16;
		salt &= 0xFF;
		inc = rst;
		inc ^= (rs & 0xFFFF);
		inc ^= salt;
		return inc;
	}

	@Override
	public int restoreSNO(int sno, int salt) {
		int rs = sno >> 16;
		rs ^= salt;
		salt &= 0xFF;
		sno &= 0xFFFF;
		sno ^= salt;
		sno ^= (rs & 0xFFFF);
		return sno;
	}

}
