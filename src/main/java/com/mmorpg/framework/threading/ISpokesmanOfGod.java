package com.mmorpg.framework.threading;

/***
 * 神的代言人，我是来接受上帝的委托，执行任务的
 */
public interface ISpokesmanOfGod extends Runnable {

	/** 停止运行 */
	public void stop();

	/** 是否还在运行 */
	public boolean isRunning();

	/** 是否是常驻服务 */
	public boolean isService();

	/** 服务名字 */
	public String getName();

	/** 执行前的初始化 */
	public void init();
}
