package com.mmorpg.framework.packet;

import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.GameSession;

/**
 * @author Ariescat
 * @version 2020/2/19 12:36
 */
public class PacketFactory {

	public static <T extends AbstractPacket> T createPacket(short packetId) {
		// TODO
		return null;
	}

	public static AbstractPacket createPacket(Request request, GameSession session) {
		return null;
	}

	public static boolean isPacketEncryptEnabled() {
		return false;
	}

	public static String getPacketNameByCommonId(short command) {
		return null;
	}
}
