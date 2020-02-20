package com.mmorpg.framework.auth;

/**
 * @author Ariescat
 * @version 2020/2/20 14:45
 */
public interface ISNOAuth {

	/**
	 * 生成发送的序列号
	 */
	int genSNO(int inc, int salt);

	/**
	 * 恢复真实的序列号
	 */
	int restoreSNO(int sno, int salt);
}
