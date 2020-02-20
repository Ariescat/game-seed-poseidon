package com.mmorpg.framework.auth.impl;

import com.mmorpg.framework.auth.ISNOAuth;

/**
 * @author Ariescat
 * @version 2020/2/20 16:53
 */
public class SNOAuth3 implements ISNOAuth {

	@Override
	public int genSNO(int inc, int salt) {
		inc <<= 5;
		inc ^= salt;
		return inc;
	}

	@Override
	public int restoreSNO(int sno, int salt) {
		sno ^= salt;
		sno >>>= 5;
		return sno;
	}
}
