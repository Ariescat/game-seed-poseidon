package com.mmorpg.framework.thread.god;

/**
 * 对神的请求
 */
public interface IRequestToGod {

	/**
	 * 执行请求
	 *
	 * @return 返回是否需要计数
	 */
	boolean execute();
}
