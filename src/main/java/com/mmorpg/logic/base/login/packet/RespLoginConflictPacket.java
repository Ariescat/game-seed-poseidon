package com.mmorpg.logic.base.login.packet;

import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.ResponsePacket;
import com.mmorpg.framework.packet.anno.Packet;

/**
 * @author Ariescat
 * @version 2020/2/28 10:44
 */
@Packet(commandId = PacketId.RESP_LOGIN_CONFLICT)
public class RespLoginConflictPacket extends ResponsePacket {

	/**
	 * 账号重复登陆
	 */
	private static final byte DUPLICATE_LOGIN = 1;

	/**
	 * 长时间空闲
	 */
	private static final byte IDLE = 2;

	private byte type;

	public void duplicateLogin() {
		this.type = DUPLICATE_LOGIN;
	}

	public void idle() {
		this.type = IDLE;
	}

	@Override
	public void doResponse(Response response) {
		response.writeByte(type);
	}
}
