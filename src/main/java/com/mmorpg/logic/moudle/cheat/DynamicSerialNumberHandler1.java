package com.mmorpg.logic.moudle.cheat;

/**
 * @author Ariescat
 * @version 2020/2/20 14:32
 */
public class DynamicSerialNumberHandler1 implements ISerialNumberHandler {

    @Override
    public int handle(int recv, int salt, int random) {
        recv = recv << 19 | recv >>> 13;
        recv ^= salt;
        recv >>>= random;
        return recv;
    }

    @Override
    public int generate(int sno, int salt, int random) {
        sno <<= random;
        sno ^= salt;
        sno = sno << 13 | sno >>> 19;
        return sno;
    }

}
