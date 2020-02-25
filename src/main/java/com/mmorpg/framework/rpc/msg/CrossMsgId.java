package com.mmorpg.framework.rpc.msg;

/**
 * @author Ariescat
 * @version 2020/2/23 10:41
 */
public interface CrossMsgId {

	// ---------------------- system ----------------------
	short RPC_RESPONSE = 1;

	// ---------------------- other modules ----------------------
	short REQ_XXX_INFO = 1001;
	short REQ_YYY_INFO = 1002;
}
