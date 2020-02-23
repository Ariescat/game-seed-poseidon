package com.mmorpg.framework.event;

/**
 * @author Ariescat
 * @version 2020/2/19 15:21
 */
public interface EventBus {

	void post(Event event);
}
