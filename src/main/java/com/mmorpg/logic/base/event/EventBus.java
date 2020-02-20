package com.mmorpg.logic.base.event;

/**
 * @author Ariescat
 * @version 2020/2/19 15:21
 */
public interface EventBus {

	void post(Event event);
}
