package com.mmorpg.framework.rpc.msg.sender;

import com.mmorpg.framework.rpc.msg.ICrossBaseMsg;

/**
 * @author Ariescat
 * @version 2020/2/23 11:34
 */
public interface ICrossMsgSender {

	void sendMsg(ICrossBaseMsg<?> msg);
}
