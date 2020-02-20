package com.mmorpg.framework.auth.impl;

import com.mmorpg.framework.auth.IBufAuth;

/**
 * @author Ariescat
 * @version 2020/2/20 14:53
 */
public class BufAuth2 implements IBufAuth {

    @Override
    public void encrypt(byte[] input, int off, int len, int xor) {
        if (len == 0) {
            return;
        }
        for (int i = 0; i < len; i++) {
            int index = i + off;
            if ((i & 1) == 1) {
                input[index] ^= ~xor;
            } else {
                input[index] ^= xor;
            }
            if (i == 1) {
                input[index] ^= (xor >> 7);
            }
            if (i == 5) {
                input[index] ^= (xor >> 19);
            }
        }
    }

    @Override
    public void decrypt(byte[] input, int off, int len, int xor) {
        if (len == 0) {
            return;
        }
        for (int i = 0; i < len; i++) {
            int index = i + off;
            if (i == 1) {
                input[index] ^= (xor >> 7);
            }
            if (i == 5) {
                input[index] ^= (xor >> 19);
            }
            if ((i & 1) == 1) {
                input[index] ^= ~xor;
            } else {
                input[index] ^= xor;
            }
        }
    }
}