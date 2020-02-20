package com.mmorpg.logic.base.utils;

/**
 * @author Ariescat
 * @version 2020/2/20 15:47
 */
public class BitUtils {

    public static int writeBit(int ori, int value, int index) {
        return ori | (value << index);
    }

    public static int writeBit(int ori, byte value, int index) {
        return ori | (value << index);
    }

    public static int readBit(int ori, int index, int len) {
        int flag = 0;
        for (int i = 0; i < len; i++) {
            flag |= 1 << i;
        }
        ori >>= index;
        return ori & flag;
    }

    /**
     * 循环左移位
     *
     * @param ori
     * @param step
     * @return
     */
    public static int cyclicLeftShift(int ori, int step) {
        return ori << step | ori >>> (32 - step);
    }

    public static long cyclicLeftShift(long ori, int step) {
        return ori << step | ori >>> (64 - step);
    }

}
