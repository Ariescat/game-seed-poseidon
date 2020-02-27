package com.mmorpg.framework.rpc.msg.packet;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mmorpg.framework.cross.BaseCrossPacket;
import com.mmorpg.framework.net.Request;
import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.packet.PacketId;
import com.mmorpg.framework.packet.anno.Packet;
import com.mmorpg.framework.rpc.msg.CrossMsgFactory;
import com.mmorpg.framework.rpc.msg.ICrossBaseMsg;
import com.mmorpg.framework.rpc.msg.ICrossMsg;
import com.mmorpg.framework.rpc.msg.ICrossPlayerMsg;
import com.mmorpg.framework.rpc.msg.sender.ChannelCrossMsgSender;
import com.mmorpg.logic.base.scene.creature.player.Player;
import io.netty.channel.Channel;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ariescat
 * @version 2020/2/23 10:49
 */
@Packet(commandId = PacketId.CROSS_PROTO_STUFF_PACKET)
public class CrossMsgPacket extends BaseCrossPacket {

	private final static Logger log = LoggerFactory.getLogger(CrossMsgPacket.class);

	private final static ThreadLocal<LinkedBuffer> linkBuffer = new ThreadLocal<LinkedBuffer>() {
		@Override
		protected LinkedBuffer initialValue() {
			return LinkedBuffer.allocate();
		}
	};

	private final static LoadingCache<String, Schema<?>> schemaCache = CacheBuilder
		.newBuilder()
		.build(new CacheLoader<String, Schema<?>>() {
			@Override
			public Schema<?> load(String key) throws Exception {
				Class<?> clazz = Class.forName(key);
				return RuntimeSchema.createFrom(clazz);
			}
		});

	private ICrossBaseMsg<?> crossMsg;

	public ICrossBaseMsg<?> getCrossMsg() {
		return crossMsg;
	}

	public static CrossMsgPacket of(ICrossBaseMsg<?> crossMsg) {
		CrossMsgPacket packet = PacketFactory.createPacket(PacketId.CROSS_PROTO_STUFF_PACKET);
		assert packet != null;
		packet.crossMsg = crossMsg;
		return packet;
	}

	@Override
	public void read(Request request) throws Exception {
		short s = request.readShort(); // 读取 msgId 或 类名长度
		String className;
		if (s <= 0) {
			CrossMsgFactory.CrossMsgMapping mapping = CrossMsgFactory.getById((short) -s);
			assert mapping != null;
			className = mapping.getClassName();
		} else {
			byte[] dst = new byte[s];
			request.readBytes(dst);
			className = new String(dst, Charsets.UTF_8);
		}
		try {
			int size = request.readInt();
			byte[] d = new byte[size];
			request.readBytes(d);
			Schema schema = schemaCache.get(className);
			assert schema != null;
			Object o = schema.newMessage();
			ProtostuffIOUtil.mergeFrom(d, o, schema);
			if (o instanceof ICrossBaseMsg) {
				crossMsg = (ICrossBaseMsg<?>) o;
			}
		} catch (Throwable e) {
			log.error("CrossMsgPacket 反序列化报错: class:{}", className, e);
			throw e;
		}
	}

	@Override
	public void doResponse(Response response) {
		LinkedBuffer linkedBuffer = linkBuffer.get();
		String className = null;
		try {
			className = crossMsg.getClass().getName();
			CrossMsgFactory.CrossMsgMapping mapping = CrossMsgFactory.getByClassName(className);
			if (null != mapping) {
				response.writeShort(-mapping.getMsgId());
			} else {
				byte[] content = className.getBytes(Charsets.UTF_8);
				response.writeShort(content.length);
				response.writeBytes(content);
			}
			Schema schema = schemaCache.get(className);
			assert schema != null;
			byte[] bytes = ProtostuffIOUtil.toByteArray(crossMsg, schema, linkedBuffer);
			response.writeInt(bytes.length);
			response.writeBytes(bytes);
		} catch (Throwable e) {
			log.error("CrossMsgPacket 序列化报错: class:{}", className, e);
		} finally {
			linkedBuffer.clear();
		}
	}

	@Override
	public void crossExecute(Channel channel) {
		if (crossMsg instanceof ICrossMsg) {
			((ICrossMsg) crossMsg).execute(new ChannelCrossMsgSender(channel));
		} else if (crossMsg instanceof ICrossPlayerMsg) {
			crossMsg.execute(null);
		}

		if (log.isDebugEnabled() && crossMsg != null) {
			log.debug("crossMsg:{}", crossMsg);
		}
	}

	@Override
	public PacketExE execute(Player player) {
		if (crossMsg instanceof ICrossPlayerMsg) {
			((ICrossPlayerMsg) crossMsg).execute(player);
		}

		if (log.isDebugEnabled() && crossMsg != null) {
			log.debug("crossMsg:{}", crossMsg.getClass());
		}
		return null;
	}

}
