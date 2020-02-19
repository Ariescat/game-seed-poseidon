package com.mmorpg.framework.rpc;

import com.google.common.util.concurrent.AbstractFuture;

/**
 * @author Ariescat
 * @version 2019/12/24 10:22
 */
public class RpcCallResponseFuture extends AbstractFuture<Object> implements RpcResponseFuture<Object> {

    // 返回给哪个请求
    private final long requestID;

    public RpcCallResponseFuture(long requestID) {
        this.requestID = requestID;
    }

    @Override
    public long getRequestID() {
        return requestID;
    }

    @Override
    public void addListener(final RpcListener listener) {
        super.addListener(new Runnable() {
            @Override
            public void run() {
                Object o;
                try {
                    o = get();
                } catch (Throwable e) {
                    o = e;
                }
                listener.onRet(o);
            }
        }, RpcFutures.currentThreadExecutor);
    }

    public void setValue(Object value) {
        super.set(value);
    }

    public void exception(Throwable throwable) {
        super.setException(throwable);
    }
}
