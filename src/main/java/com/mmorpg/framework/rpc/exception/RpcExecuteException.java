package com.mmorpg.framework.rpc.exception;

/**
 * @author Ariescat
 * @version 2019/12/24 10:17
 */
public class RpcExecuteException extends Exception {
    public RpcExecuteException(String error) {
        super(error, null, false, false);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
