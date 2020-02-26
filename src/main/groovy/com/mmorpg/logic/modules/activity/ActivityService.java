package com.mmorpg.logic.modules.activity;

import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/26 14:41
 */
@Component
public class ActivityService implements IActivityService {

	@Override
	public void reqInfo() {
		System.err.println("reqInfo");
	}
}
