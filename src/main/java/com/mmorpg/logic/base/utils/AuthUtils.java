package com.mmorpg.logic.base.utils;

/**
 * @author Ariescat
 * @version 2020/2/19 15:12
 */
public class AuthUtils {
    private static IKeyAuth KEY = new KeyAuth2();
    private static ISNOAuth SNO = new SNOAuth1();
    private static IBufAuth BUF = new BufAuth2();

    public static int genKey() {
        return KEY.genKey();
    }

    public static int restoreSalt(int key) {
        return KEY.restoreSalt(key);
    }

    public static int genSNO(int inc, int salt) {
        return SNO.genSNO(inc, salt);
    }

    public static int restoreSNO(int sno, int salt) {
        return SNO.restoreSNO(sno, salt);
    }

    public static void encrypt(byte[] input, int off, int len, int xor) {
        BUF.encrypt(input, off, len, xor);
    }

    public static void decrypt(byte[] input, int off, int len, int xor) {
        BUF.decrypt(input, off, len, xor);
    }

    public static int snoDoubleCheck(int len, int sno) {
        return (len >> 1) + (sno >> 3);
    }

}
