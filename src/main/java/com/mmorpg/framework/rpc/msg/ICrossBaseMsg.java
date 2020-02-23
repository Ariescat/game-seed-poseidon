package com.mmorpg.framework.rpc.msg;

/**
 * 请不要直接使用这个接口（已去掉public修饰符）
 * 请使用 {@link ICrossMsg} 或 {@link ICrossPlayerMsg}
 *
 * @author Ariescat
 * @version 2020/2/23 11:23
 */
public interface ICrossBaseMsg<Sender> {

	/**
	 * 消息处理
	 *
	 * @param sender 消息发送端
	 */
	void execute(Sender sender);
}
