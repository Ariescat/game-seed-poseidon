package com.mmorpg.framework.auth;

/**
 * @author Ariescat
 * @version 2020/2/20 14:41
 */
public interface IKeyAuth {

    /**
     * 生成Key
     */
    int genKey();

    /**
     * 还原Key
     */
    int restoreSalt(int key);
}
