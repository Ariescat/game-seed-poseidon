package com.mmorpg.framework.packet;

import com.mmorpg.framework.net.ByteBufRequest;
import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.exception.PacketDecodeException;
import com.mmorpg.framework.utils.AuthUtils;
import com.mmorpg.framework.utils.crc32.CRC32Utils;
import io.netty.buffer.ByteBuf;

/**
 * 数据包解密工具类
 *
 * @author Ariescat
 * @version 2020/3/2 10:44
 */
public class PacketDecoder {

	/**
	 * 数据解密
	 */
	public static Request decode(Request request, GameSession session, boolean needDecode) throws PacketDecodeException {
		if (request instanceof ByteBufRequest) {
			ByteBufRequest byteBufRequest = (ByteBufRequest) request;
			ByteBuf buf = byteBufRequest.getByteBuf();

			long recvCRC32 = buf.readUnsignedInt();
			int recvSerialNo = buf.getInt(buf.readerIndex()); // 获取序列号，此时读下标没有移动，因为序列号还要用到后面计算CRC32

			session.checkAvailableSerialNo(recvSerialNo, needDecode);

			if (needDecode) {
				int readableBytes = buf.readableBytes();
				if (buf.hasArray()) {
					int curIndex = buf.arrayOffset() + buf.readerIndex();
					byte[] array = buf.array();
					/*
					 * 前面的序列号4个字节不参与decrypt
					 * 最后4个字节是随机发的无意义int，不参与decrypt
					 * 所以起点+4，总长度-8
					 */
					AuthUtils.decrypt(array, curIndex + 4, readableBytes - 8, session.getSalt());
					/*
					 * 最后4个字节不参与计算CRC32
					 * 所以总长度-4
					 */
					long CRC32 = CRC32Utils.calCRC32(array, curIndex, readableBytes - 4);
					if (CRC32 != recvCRC32) {
						throw new PacketDecodeException("CRC32 different: recv:" + recvCRC32 + ", cal:" + CRC32);
					}
				} else {
					byte[] array = new byte[readableBytes - 4]; // 最后4个字节是随机发的无意义int
					buf.getBytes(buf.readerIndex(), buf);
					AuthUtils.decrypt(array, 4, array.length - 4, session.getSalt()); // 前面的序列号4个字节不参与decrypt
					long CRC32 = CRC32Utils.calCRC32(array);
					if (CRC32 != recvCRC32) {
						throw new PacketDecodeException("CRC32 different: recv:" + recvCRC32 + ", cal:" + CRC32);
					}
					buf.setBytes(buf.readerIndex() + 4, array, 4, array.length - 4); // decrypt后的内容写回
				}
			}
			buf.readInt(); // 读序列号，读下标移动到真正的包内容
			return byteBufRequest;
		}
		return null;
	}
}
