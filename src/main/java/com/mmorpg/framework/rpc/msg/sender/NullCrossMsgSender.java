package com.mmorpg.framework.rpc.msg.sender;

import com.mmorpg.framework.rpc.msg.ICrossBaseMsg;

/**
 * @author Ariescat
 * @version 2020/2/26 11:57
 */
public class NullCrossMsgSender implements ICrossMsgSender {

	public static final NullCrossMsgSender INSTANCE = new NullCrossMsgSender();

	private NullCrossMsgSender() {
	}

	@Override
	public void sendMsg(ICrossBaseMsg<?> msg) {

	}
}
