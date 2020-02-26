package com.mmorpg.framework.rpc.example.msg;

import com.mmorpg.framework.rpc.msg.CrossMsgId;
import com.mmorpg.framework.rpc.msg.ICrossMsg;
import com.mmorpg.framework.rpc.msg.anno.CrossMsg;
import com.mmorpg.framework.rpc.msg.sender.ICrossMsgSender;

/**
 * @author Ariescat
 * @version 2020/2/26 13:12
 */
@CrossMsg(CrossMsgId.REQ_XXX_INFO)
public class ReqTestMsgWithID extends ICrossMsg {

	private String data;

	public ReqTestMsgWithID(String data) {
		this.data = data;
	}

	@Override
	public void execute(ICrossMsgSender sender) {
		sender.sendMsg(new RespTestMsg("server receive:" + data));
	}
}
