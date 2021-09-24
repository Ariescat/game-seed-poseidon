package com.mmorpg.logic.modules.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/26 14:41
 */
@Component
public class ActivityService2 implements IActivityService2 {

//	/**
//	 * TODO 测试循环依赖
//	 */
//	@Autowired
//	private IActivityService activityService;

	@Override
	public void reqInfo() {
		System.err.println("reqInfo2");
	}
}
