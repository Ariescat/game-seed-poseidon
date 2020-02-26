package com.mmorpg.framework.event;

import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/26 22:04
 */
@Component
public class DefaultEventBus implements EventBus {

	@Override
	public void post(Event event) {
		// TODO
		System.err.println("event" + event);
	}
}
