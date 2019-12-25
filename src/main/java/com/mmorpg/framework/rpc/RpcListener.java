package com.mmorpg.framework.rpc;

/**
 * @author Ariescat
 * @version 2019/12/24 10:23
 */
public interface RpcListener {

    /**
     * @param ret 可能时异常 在IO线程执行
     *            Exception or msg
     */
    void onRet(Object ret);
}
