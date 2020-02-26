package com.mmorpg.framework.rpc.example.msg;

import com.mmorpg.framework.cross.RemoteServers;

/**
 * @author Ariescat
 * @version 2020/2/26 13:09
 */
public class Test {

	public void test() {
		RemoteServers.sendCrossMsgToCenter(new ReqTestMsg("hello, i am client"));
	}
}
