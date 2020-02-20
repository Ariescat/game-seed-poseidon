package com.mmorpg.logic.base.message;

/**
 * @author Ariescat
 * @version 2020/2/19 12:13
 */
public enum MessageId {

	ANOUNCEMENT(10000);

	private int id;

	MessageId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
