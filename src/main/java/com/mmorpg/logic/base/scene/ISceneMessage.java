package com.mmorpg.logic.base.scene;

/**
 * 场景消息
 *
 * @author Ariescat
 * @version 2020/3/3 14:37
 */
public interface ISceneMessage extends IMessage {

	void execute(Scene curScene);
}
