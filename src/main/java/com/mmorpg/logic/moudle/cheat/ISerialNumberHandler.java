package com.mmorpg.logic.moudle.cheat;

/**
 * @author Ariescat
 * @version 2020/2/20 14:30
 */
public interface ISerialNumberHandler {

    int handle(int recv, int salt, int random);

    int generate(int recv, int salt, int random);
}
