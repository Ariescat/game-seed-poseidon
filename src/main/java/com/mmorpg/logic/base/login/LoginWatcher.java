package com.mmorpg.logic.base.login;

import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.domain.Player;
import com.mmorpg.logic.base.message.Message;
import com.mmorpg.logic.base.message.MessageId;
import com.mmorpg.logic.base.message.MessageType;
import com.mmorpg.logic.base.packet.RespLoginAskPacket;
import com.mmorpg.logic.base.packet.RespMessagePacket;
import com.mmorpg.logic.base.utils.OnlinePlayer;
import com.mmorpg.logic.base.utils.PacketUtils;
import com.mmorpg.logic.base.utils.TimeUtils;
import com.mmorpg.logic.base.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Ariescat
 * @version 2020/2/19 11:32
 */
public class LoginWatcher {

	private final static Logger log = LoggerFactory.getLogger(LoginWatcher.class);

	/**
	 * 登录等待请求
	 */
	private final ConcurrentLinkedQueue<LoginPendingRequest> pendingQueue = new ConcurrentLinkedQueue<>();

	private final static LoginWatcher instance = new LoginWatcher();

	public static LoginWatcher getInstance() {
		return instance;
	}

	private LoginWatcher() {
	}

	/**
	 * 处理登录逻辑
	 */
	public final void processLogin(Player player, GameSession session, RespLoginAskPacket packet) {
		final int max = Context.getConfigService().getMaxOnlineCount();
		if (max > 0 && OnlinePlayer.getInstance().getOnlinePlayerCount() >= max) {
			RespMessagePacket msgPacket = Message.buildMessagePacket(MessageType.WARN, MessageId.ANOUNCEMENT, "服务器繁忙，请尝试登录其他服务器！");
			PacketUtils.sendAndClose(session.getChannel(), msgPacket);
			return;
		}
		if (OnlinePlayer.getInstance().isSameIPMax(session.getIP())) {
			RespMessagePacket msgPacket = Message.buildMessagePacket(MessageType.WARN, MessageId.ANOUNCEMENT, "相同IP登录超过上限！");
			PacketUtils.sendAndClose(session.getChannel(), msgPacket);
			return;
		}

		if (OnlinePlayer.getInstance().registerSession(player, session)) {
			//首次登录不需要进入等待队列，直接处理掉
			handleLoginLogic(player, session, packet);
		} else {
			/*
			 * 1)玩家账号被顶号，需要先执行完成旧链接的退出事务，才开启新的登录流程
			 * 2)玩家正常退出后，由于ReqL ogoutPacket是异步执行，必须保证退出事务处理完毕后才能开启新的登录流程
			 */
			pendingQueue.add(new LoginPendingRequest(player, session, packet));
		}
	}


	private Timer logTimer = new Timer(1, TimeUnit.SECONDS);

	public final void heartbeat() {
		if (pendingQueue.isEmpty()) {
			return;
		}
		if (logTimer.isTimeOut(TimeUtils.getCurrentMillisTime())) {
			log.info("{} Pending Size:{}", Thread.currentThread().getName(), pendingQueue.size());
		}
		Iterator<LoginPendingRequest> iterator = pendingQueue.iterator();
		while (iterator.hasNext()) {
			LoginPendingRequest request = iterator.next();
			if (request.execute()) {
				iterator.remove();
			} else if (request.getCount() > 3) {
				iterator.remove();
			} else if (request.isTimeout()) {
				request.reset();
			}
		}
	}

	private void handleLoginLogic(Player player, GameSession session, RespLoginAskPacket packet) {

	}

	/**
	 * 登录等待请求
	 */
	public class LoginPendingRequest {

		private Player player;
		private GameSession session;
		private RespLoginAskPacket packet;
		private long timeout;
		private int count = 0;

		public LoginPendingRequest(Player player, GameSession session, RespLoginAskPacket packet) {
			this.player = player;
			this.session = session;
			this.packet = packet;
			this.timeout = TimeUtils.getCurrentMillisTime() + 20000;
		}

		public boolean isTimeout() {
			return TimeUtils.getCurrentMillisTime() > timeout;
		}

		public void reset() {
			count++;
			OnlinePlayer.getInstance().timeoutReset(player, GameSessionStatusUpdateCause.TimeoutReset);
		}

		public boolean execute() {
			if (OnlinePlayer.getInstance().registerSession(player, session)) {
				handleLoginLogic(player, session, packet);
				return true;
			}
			return false;
		}

		public int getCount() {
			return count;
		}
	}

}
