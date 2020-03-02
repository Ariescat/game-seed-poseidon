package com.mmorpg.framework.packet.exception;

/**
 * @author Ariescat
 * @version 2020/3/2 9:58
 */
public class PacketDecodeException extends Exception {

	public PacketDecodeException(String message) {
		super(message);
	}

	public PacketDecodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
