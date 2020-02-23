package com.mmorpg.framework.packet;

/**
 * @author Ariescat
 * @version 2020/2/19 12:36
 */
public class PacketFactory {

	public static <T extends AbstractPacket> T createPacket(short packetId) {
		return null;
	}

	public static boolean isPacketEncryptEnabled() {
		return false;
	}
}
