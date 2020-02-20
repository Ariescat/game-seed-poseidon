package com.mmorpg.framework.auth.impl;

import com.mmorpg.framework.auth.IBufAuth;

/**
 * @author Ariescat
 * @version 2020/2/20 14:47
 */
public class BufAuth1 implements IBufAuth {

    @Override
    public final void encrypt(byte[] input, int off, int len, int xor) {
        if (len == 0) {
            return;
        }
        for (int i = 0; i < len; i++) {
            int index = i + off;
            input[index] ^= xor;
            input[index] ^= i;
        }
    }

    @Override
    public final void decrypt(byte[] input, int off, int len, int xor) {
        if (len == 0) {
            return;
        }
        for (int i = 0; i < len; i++) {
            int index = i + off;
            input[index] ^= i;
            input[index] ^= xor;
        }
    }
}
