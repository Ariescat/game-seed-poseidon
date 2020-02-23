package com.mmorpg.framework.packet;

import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.utils.TimeUtils;
import com.mmorpg.logic.base.domain.Player;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Ariescat
 * @version 2020/2/19 11:43
 */
public abstract class AbstractPacket {

	private final static Logger log = LoggerFactory.getLogger(AbstractPacket.class);

	/**
	 * 协议号
	 */
	protected short cmd;

	/**
	 * 创建时间
	 */
	protected long createTime = TimeUtils.getCurrentMillisTime();

	public Short getCommand() {
		return cmd;
	}

	public void setCommand(short id) {
		this.cmd = id;
	}

	public long getCreateTime() {
		return createTime;
	}

	public boolean isAdmin() {
		return cmd < 1000;
	}

	public boolean isCross() {
		return cmd > 30000;
	}


	public abstract void read(Request request) throws Exception;

	public abstract PacketExE executeBeforeLogin(GameSession session);

	public abstract PacketExE execute(Player player);

	public void crossExecute(Channel channel) {

	}

	public abstract void doResponse(Response response);

	public Response write() {
		// TODO
		return null;
	}

	@Override
	public String toString() {
		return "Packet [" + cmd + "," + this.getClass().getSimpleName() + "]";
	}

	protected static final PacketExE NULL_EXE = null;

	public enum PacketExE {
		PACKET_EXE_ERROR(1), //表示出现严重错误，当前连接需要被强制断开
		PACKET_EXE_BREAK(2), //表示返回后剩下的消息将不在当前处理循环里处理
		PACKET_EXE_CONTINUE(3); //表示继续在当前循环里执行剩下的消息

		private int value;

		private PacketExE(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
