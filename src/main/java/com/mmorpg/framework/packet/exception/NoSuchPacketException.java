package com.mmorpg.framework.packet.exception;

/**
 * @author Ariescat
 * @version 2020/3/2 9:48
 */
public class NoSuchPacketException extends NullPointerException {

	public NoSuchPacketException(short packetId) {
		super("not such packet [" + packetId + "]");
	}
}
