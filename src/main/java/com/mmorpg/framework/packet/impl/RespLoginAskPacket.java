package com.mmorpg.framework.packet.impl;

import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.ResponsePacket;
import com.mmorpg.framework.packet.anno.Packet;

/**
 * @author Ariescat
 * @version 2020/2/19 12:02
 */
@Packet(commandId = PacketId.RESP_LOGIN_ASK)
public class RespLoginAskPacket extends ResponsePacket {

	private static final byte SUCCESS = 1;     // 成功
	private static final byte FAILED = 2;      // 失败
	private static final byte NOT_REGISTER = 3;// 未注册
	private static final byte FORBIDDEN = 4;   // 封号
	private static final byte SECURITY = 5;    // 安全密码验证未通过

	private byte result;

	public RespLoginAskPacket success() {
		result = SUCCESS;
		return this;
	}

	public RespLoginAskPacket fail() {
		result = FAILED;
		return this;
	}

	public RespLoginAskPacket fail4notRegister() {
		result = NOT_REGISTER;
		return this;
	}

	public RespLoginAskPacket fail4forbidden() {
		result = FORBIDDEN;
		return this;
	}

	public RespLoginAskPacket fail4security() {
		result = SECURITY;
		return this;
	}

	@Override
	public void doResponse(Response response) {
		response.writeByte(result);
	}
}
