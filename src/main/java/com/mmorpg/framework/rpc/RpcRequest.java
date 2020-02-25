package com.mmorpg.framework.rpc;

import com.koloboke.function.Consumer;
import com.mmorpg.framework.rpc.msg.ICrossMsg;
import com.mmorpg.framework.rpc.msg.sender.ICrossMsgSender;
import io.protostuff.Exclude;

/**
 * @author Ariescat
 * @version 2020/2/23 10:15
 */
public abstract class RpcRequest extends ICrossMsg {
	private long requestID;
	@Exclude
	private transient long creatTime;

	public RpcRequest() {
		this.creatTime = System.currentTimeMillis();
	}

	@Override
	public void execute(final ICrossMsgSender crossMsgSender) {
		executeRequestAsync(new Consumer<RpcResponse>() {
			@Override
			public void accept(RpcResponse rpcResponse) {
				rpcResponse.setReqID(requestID);
				crossMsgSender.sendMsg(rpcResponse);
			}
		});
	}

	protected abstract void executeRequestAsync(Consumer<RpcResponse> consumer);

	public long getRequestID() {
		return requestID;
	}

	public void setRequestID(long requestID) {
		this.requestID = requestID;
	}

	public long getCreatTime() {
		return creatTime;
	}

}
