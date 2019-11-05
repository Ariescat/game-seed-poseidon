package com.mmorpg.framework.threading.policy.impl;

import com.mmorpg.framework.threading.IRequestToGod;
import com.mmorpg.framework.threading.Sprite;
import com.mmorpg.framework.threading.policy.IRequestOverflowPolicy;

public class PollBeforeAddPolicy implements IRequestOverflowPolicy {
    @Override
    public void handle(Sprite sprite, IRequestToGod request) {
        sprite.poll();
        sprite.add(request);
    }
}
