package com.mmorpg.framework.rpc.msg;

import com.google.common.collect.ImmutableMap;
import com.koloboke.collect.map.ShortObjMap;
import com.koloboke.collect.map.hash.HashShortObjMaps;

import java.util.Map;

/**
 * @author Ariescat
 * @version 2020/2/23 14:30
 */
public class CrossMsgFactory {

	private static Holder HOLDER;

	public static CrossMsgMapping getById(short id) {
		if (HOLDER == null) {
			return null;
		}
		return HOLDER.id2mapping.get(id);
	}

	public static CrossMsgMapping getByClassName(String clazzName) {
		if (HOLDER == null) {
			return null;
		}
		return HOLDER.className2mapping.get(clazzName);
	}

	static void init(Map<Short, Class<? extends ICrossBaseMsg>> msgs) {
		HOLDER = new Holder(msgs);
	}

	public static class CrossMsgMapping {
		private final short msgId;
		private final String className;

		private CrossMsgMapping(Short msgId, Class<? extends ICrossBaseMsg> msg) {
			this.msgId = msgId;
			this.className = msg.getName();
		}

		public short getMsgId() {
			return msgId;
		}

		public String getClassName() {
			return className;
		}
	}

	private static final class Holder {

		private final ShortObjMap<CrossMsgMapping> id2mapping;
		private final ImmutableMap<String, CrossMsgMapping> className2mapping;

		private Holder(Map<Short, Class<? extends ICrossBaseMsg>> map) {
			ImmutableMap.Builder<String, CrossMsgMapping> builder = ImmutableMap.builder();
			id2mapping = HashShortObjMaps.newUpdatableMap();
			for (Map.Entry<Short, Class<? extends ICrossBaseMsg>> entry : map.entrySet()) {
				String clazzName = entry.getValue().getName();
				CrossMsgMapping msg = new CrossMsgMapping(entry.getKey(), entry.getValue());
				id2mapping.put(entry.getKey().shortValue(), msg);
				builder.put(clazzName, msg);
			}
			className2mapping = builder.build();
		}

	}
}
