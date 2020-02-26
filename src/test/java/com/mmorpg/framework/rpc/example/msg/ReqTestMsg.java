package com.mmorpg.framework.rpc.example.msg;

import com.mmorpg.framework.rpc.msg.ICrossMsg;
import com.mmorpg.framework.rpc.msg.sender.ICrossMsgSender;

/**
 * @author Ariescat
 * @version 2020/2/26 13:12
 */
public class ReqTestMsg extends ICrossMsg {

	private String data;

	public ReqTestMsg(String data) {
		this.data = data;
	}

	@Override
	public void execute(ICrossMsgSender sender) {
		sender.sendMsg(new RespTestMsg("server receive:" + data));
	}
}
