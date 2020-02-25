package com.mmorpg.framework.rpc;

import com.mmorpg.framework.rpc.exception.RpcExecuteException;
import com.mmorpg.framework.rpc.msg.CrossMsgId;
import com.mmorpg.framework.rpc.msg.ICrossMsg;
import com.mmorpg.framework.rpc.msg.anno.CrossMsg;
import com.mmorpg.framework.rpc.msg.sender.ICrossMsgSender;
import com.mmorpg.framework.utils.ExceptionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ariescat
 * @version 2020/2/25 15:26
 */
@CrossMsg(CrossMsgId.REQ_XXX_INFO)
public class RpcResponse extends ICrossMsg {

	private final static Logger log = LoggerFactory.getLogger(RpcResponse.class);

	private long reqID;
	private Object ret;
	private String error;

	@Override
	public void execute(ICrossMsgSender crossMsgSender) {
		try {
			RpcCallResponseFuture responseFuture = RpcFutures.getAndRemoveResponseFuture(reqID);
			if (responseFuture == null) {
				RpcRequest rpcRequest = RpcFutures.getAndRemoveRpcRequest(reqID);
				if (rpcRequest == null) {
					// lost
					log.warn("RPC Call back, but RpcCallResponseFuture lost, and RpcRequest isCleared or return Twice. Response:{}",
						this);
				} else {
					log.warn("RPC Call back, but RpcCallResponseFuture lost, useTime [{}], Request:{}, Response:{}",
						System.currentTimeMillis() - rpcRequest.getCreatTime(), rpcRequest, this);
				}
			} else {
				if (StringUtils.isNotBlank(error)) {
					RpcExecuteException throwable = new RpcExecuteException(error);
					responseFuture.exception(throwable);
					ExceptionUtils.log("RPC Call Exception:", error);
				} else {
					responseFuture.setValue(ret);
				}
			}
		} catch (Exception e) {
			ExceptionUtils.log(e);
		}
	}

	public void setReqID(long reqID) {
		this.reqID = reqID;
	}

	public void setRet(Object ret) {
		this.ret = ret;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
			.append(this.getClass().getName())
			.append("{")
			.append("reqId=").append(reqID)
			.append(",ret=").append(ret);
		if (error != null) {
			sb.append(",error='").append(error).append('\'');
		}
		sb.append("}");
		return sb.toString();
	}
}
