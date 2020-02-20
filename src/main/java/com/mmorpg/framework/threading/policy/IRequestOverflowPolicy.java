package com.mmorpg.framework.threading.policy;

import com.mmorpg.framework.threading.IRequestToGod;
import com.mmorpg.framework.threading.Sprite;

public interface IRequestOverflowPolicy {

	void handle(Sprite sprite, IRequestToGod request);
}
