package com.mmorpg.logic.base.message;

import com.mmorpg.logic.base.packet.PacketFactory;
import com.mmorpg.logic.base.packet.PacketId;
import com.mmorpg.logic.base.packet.RespMessagePacket;

/**
 * @author Ariescat
 * @version 2020/2/19 12:14
 */
public class Message {

    public static RespMessagePacket buildMessagePacket(MessageType messageType, MessageId msgId, Object... args) {
        RespMessagePacket packet = PacketFactory.createPacket(PacketId.RESP_MESSAGE);
        packet.init(messageType, msgId, args);
        return packet;
    }
}
