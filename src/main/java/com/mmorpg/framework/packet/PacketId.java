package com.mmorpg.framework.packet;

/**
 * 包协议号
 *
 * @author Ariescat
 * @version 2020/2/19 12:34
 */
public class PacketId {

	/**
	 * 请求登录验证
	 */
	public static final short REQ_LOGIN_AUTH = 10101;

	/**
	 * =============== utils ===============
	 */
	public static final short RESP_MESSAGE = 13301;
	public static final short RESP_LOGIN_CONFLICT = 13303;

	/**
	 * =============== cross ===============
	 */
	public static final short CROSS_PROTO_STUFF_PACKET = 30001;
}
