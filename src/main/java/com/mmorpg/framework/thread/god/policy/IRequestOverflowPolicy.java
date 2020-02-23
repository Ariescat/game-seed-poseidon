package com.mmorpg.framework.thread.god.policy;

import com.mmorpg.framework.thread.god.IRequestToGod;
import com.mmorpg.framework.thread.god.Sprite;

public interface IRequestOverflowPolicy {

	void handle(Sprite sprite, IRequestToGod request);
}
