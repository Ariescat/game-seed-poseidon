package com.mmorpg.framework.utils;

/**
 * 架构常量
 *
 * @author Ariescat
 * @version 2020/2/19 11:29
 */
public interface Constant {

	/**
	 * 两倍CPU核数
	 */
	int TWICE_CPU = Runtime.getRuntime().availableProcessors() * 2;

	long MAX_48BIT = 1L << 47;
	long MAX_48BIT_NEGATIVE = -MAX_48BIT;
}
