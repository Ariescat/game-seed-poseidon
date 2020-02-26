package com.mmorpg.framework.rpc.example.msg;

import com.mmorpg.framework.rpc.msg.ICrossMsg;
import com.mmorpg.framework.rpc.msg.sender.ICrossMsgSender;

/**
 * @author Ariescat
 * @version 2020/2/26 13:15
 */
public class RespTestMsg extends ICrossMsg {

	private String retData;

	public RespTestMsg(String retData) {
		this.retData = retData;
	}

	@Override
	public void execute(ICrossMsgSender crossMsgSender) {
		System.err.println("resp:" + retData);
	}
}
