package com.mmorpg.framework.packet;

import com.koloboke.collect.map.ShortObjMap;
import com.mmorpg.framework.cross.DispatchPacket;
import com.mmorpg.framework.cross.RemoteServers;
import com.mmorpg.framework.cross.client.CrossInfo;
import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.exception.NoSuchPacketException;
import com.mmorpg.framework.packet.exception.PacketDecodeException;
import com.mmorpg.logic.base.Context;
import io.netty.buffer.ByteBuf;

/**
 * @author Ariescat
 * @version 2020/2/19 12:36
 */
public class PacketFactory {

	private static ShortObjMap<Class<? extends AbstractPacket>> packets;

	private static ShortObjMap<Boolean> crossPackets;

	public static void init(ShortObjMap<Class<? extends AbstractPacket>> packets, ShortObjMap<Boolean> crossPackets) {
		PacketFactory.packets = packets;
		PacketFactory.crossPackets = crossPackets;
	}

	/**
	 * 判断加密开关是否开启
	 */
	public static boolean isPacketEncryptEnabled() {
		return Context.it().configService.isEncryptEnabled();
	}

	/**
	 * 通过消息ID创建一个包
	 */
	public static <T extends AbstractPacket> T createPacket(short packetId) {
		Class<? extends AbstractPacket> clazz = packets.get(packetId);
		if (clazz == null) {
			throw new NoSuchPacketException(packetId);
		}
		try {
			AbstractPacket newInstance = clazz.newInstance();
			newInstance.setCommand(packetId);
			//noinspection unchecked
			return (T) newInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过读取buffer中的消息ID和其他数据创建一个包。读取方法需要由每个包的read(ChannelBuffer buffer)决定
	 */
	public static <T extends AbstractPacket> T decodePacket(Request request, GameSession session) throws Exception {
		short packetId = request.getPacketId();
		try {
			AbstractPacket createPacket = createPacket(packetId);
			if (createPacket != null) {
//				int paramsLen = request.getByteBuf().readableBytes() - 12; // 协议参数长度 （crc4位 sno4位 第二次sno4位）
				boolean needEncrypt = isNeedEncrypt(session, createPacket);
				if (needEncrypt) {
					request = PacketDecoder.decode(request, session, true);
				}

				if (session != null) {
					CrossInfo crossInfo = session.getCrossInfo();
					if (crossInfo != null && crossInfo.isCrossed() && isCrossPacket(packetId)) {
						ByteBuf ori = request.getByteBuf();
						ByteBuf byteBuf = ori.readBytes(ori.readableBytes() - 4);// TODO
						DispatchPacket dispatchPacket = new DispatchPacket(session.getUid(), packetId, byteBuf);
						RemoteServers
							.getCrossClient(crossInfo.getCrossType(), crossInfo.getPlatform(), crossInfo.getServerId())
							.sendPacket(dispatchPacket);
						return null;
					}
				}

				createPacket.read(request);

				if (needEncrypt) {
					int sno = request.readInt();
//					int expect = AuthUtils.snoDoubleCheck(paramsLen, session.getSerialNO());
//					if (expect != sno) {
//						throw new PacketDecodeException("sno double not check, recv:" + sno + ",expect:" + expect);
//					}
				}
			}
			//noinspection unchecked
			return (T) createPacket;
		} catch (Exception e) {
			throw new PacketDecodeException("packetId: " + packetId, e);
		}
	}

	private static boolean isNeedEncrypt(GameSession session, AbstractPacket packet) {
		if (session == null) {
			return false;
		}
		// 管理后台的包，跨服的包不加密
		if (packet.isAdmin() || packet.isCross()) {
			return false;
		}
		if (packet.getCommand() == PacketId.REQ_LOGIN_AUTH) {
			return false;
		}
		return isPacketEncryptEnabled();
	}

	public static Class getClassByCommonId(short command) {
		return packets.get(command);
	}

	public static String getPacketNameByCommonId(short command) {
		Class<? extends AbstractPacket> clazz = packets.get(command);
		return clazz != null ? clazz.getSimpleName() : String.valueOf(command);
	}

	public static boolean isCrossPacket(short packetId) {
		return crossPackets.containsKey(packetId);
	}
}
