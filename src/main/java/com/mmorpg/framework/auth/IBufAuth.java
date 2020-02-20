package com.mmorpg.framework.auth;

/**
 * @author Ariescat
 * @version 2020/2/20 14:41
 */
public interface IBufAuth {

    /**
     * 加密
     */
    void encrypt(byte[] input, int off, int len, int xor);

    /**
     * 解密
     */
    void decrypt(byte[] input, int off, int len, int xor);
}
