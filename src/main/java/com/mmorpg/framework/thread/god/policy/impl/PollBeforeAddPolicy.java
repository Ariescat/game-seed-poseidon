package com.mmorpg.framework.thread.god.policy.impl;

import com.mmorpg.framework.thread.god.IRequestToGod;
import com.mmorpg.framework.thread.god.Sprite;
import com.mmorpg.framework.thread.god.policy.IRequestOverflowPolicy;

public class PollBeforeAddPolicy implements IRequestOverflowPolicy {
	@Override
	public void handle(Sprite sprite, IRequestToGod request) {
		sprite.poll();
		sprite.add(request);
	}
}
