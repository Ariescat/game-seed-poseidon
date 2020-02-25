package com.mmorpg.logic.base.cheat;

import com.mmorpg.framework.utils.random.RandomUtils;

/**
 * @author Ariescat
 * @version 2020/2/20 14:32
 */
public class DynamicSerialNumberHandler2 implements ISerialNumberHandler {

	@Override
	public int handle(int recv, int salt, int random) {
		salt += random;
		salt &= 0xFF;
		int rs = recv >> 16;
		rs ^= salt;
		recv &= 0xFFFF;
		recv ^= (rs & 0xFFF);
		return recv;
	}

	@Override
	public int generate(int sno, int salt, int random) {
		salt += random;
		salt &= 0xFF;
		int rs = RandomUtils.nextInt(32767);
		int rst = (rs ^ salt) << 16;
		sno ^= (rs & 0xFFFF);
		sno |= rst;
		return sno;
	}
}
