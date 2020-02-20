package com.mmorpg.framework.threading.policy.impl;

import com.mmorpg.framework.threading.IRequestToGod;
import com.mmorpg.framework.threading.Sprite;
import com.mmorpg.framework.threading.policy.IRequestOverflowPolicy;

public class IgnorePolicy implements IRequestOverflowPolicy {
	@Override
	public void handle(Sprite sprite, IRequestToGod request) {

	}
}
