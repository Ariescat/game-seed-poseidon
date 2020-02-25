package com.mmorpg.framework.rpc;

/**
 * @author Ariescat
 * @version 2019/12/24 10:23
 */
public interface RpcListener {

	/**
	 * @param ret retMsg or Exception 在IO线程执行
	 */
	void onRet(Object ret);
}
