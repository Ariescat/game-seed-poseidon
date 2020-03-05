package com.mmorpg.logic.base.login.packet;

import com.google.common.base.Preconditions;
import com.mmorpg.framework.http.HttpUtils;
import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.net.session.GameSessionStatus;
import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.RequestBeforeLoginPacket;
import com.mmorpg.framework.packet.anno.Packet;
import com.mmorpg.framework.utils.PacketUtils;
import com.mmorpg.logic.base.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 验证账号登录参数，并把相应的参数保存进GameSession
 *
 * @author Ariescat
 * @version 2020/3/3 14:27
 */
@Slf4j
@Packet(commandId = PacketId.REQ_LOGIN_AUTH)
public class ReqLoginAuthPacket extends RequestBeforeLoginPacket {

	// 1234 得到 DigestUtils.md5DigestAsHex("1234".getBytes())
	private static String v_string = "81dc9bdb52d04dc20036dbd8313ed055";
	private static int key = 10101 >> 1;

	private String account;     // 平台账号
	private String serverId;    // 服务器ID
	private String time;        // 时间戳 秒数
	private String isAdult;     // 是否成年
	private String client;      // 登录类型
	private String platform;    // 平台
	private String params;      // 其他参数
	private String sign;        // MD5验证码
	private String password;    // 安全密码

	@Override
	public void read(Request request) throws Exception {
		String salt = request.readString();
		Preconditions.checkArgument(v_string.equals(salt), "%s %s", v_string, salt);
		this.platform = request.readString();
		this.account = request.readString();
		this.isAdult = request.readString();
		this.serverId = request.readString();
		this.client = request.readString();
		this.time = request.readString();
		this.sign = request.readString();
		this.params = request.readString();
		this.password = request.readString();
		int salt2 = request.readInt();
		Preconditions.checkArgument(key == salt2, "%s %s", key, salt2);
	}

	@Override
	public PacketExE executeBeforeLogin(GameSession session) {
		log.info("account:{},serverId:{},isAdult:{},serverId:{},client:{},time:{},sign:{},params:{},password:{}",
			account, serverId, isAdult, serverId, client, time, sign, params, password);

		RespLoginAuthPacket packet = PacketFactory.createPacket(PacketId.RESP_LOGIN_AUTH);
//		if (Context.it().configService.isServerOpen()) {
//			packet.serverClose();
//			PacketUtils.sendAndClose(session.getChannel(), packet);
//			return null;
//		}

		// TODO 参数校验 判空之类

		Map<String, String> paramsMap = HttpUtils.parseQueryString(params);

		session.compareAndSetStatus(GameSessionStatus.INIT, GameSessionStatus.LOGIN_AUTH);
		session.setAccount(account);
		session.setServer(serverId);
		session.setAl(isAdult.trim().equals("1"));
		session.setClient(client);
		session.setPlatform(platform);
		session.setParams(params);
		session.generateKey();
		session.randomStartSNO();

//		packet.success();
//		packet.setKey(session.getKey());
//		packet.setSerialNO(session.getSerialNO());
//		packet.setSwf()

		session.sendPacket(packet);

		session.authed();
		return null;
	}

}
