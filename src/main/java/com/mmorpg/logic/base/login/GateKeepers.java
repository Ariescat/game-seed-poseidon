package com.mmorpg.logic.base.login;

import com.mmorpg.framework.utils.Constant;
import com.mmorpg.framework.net.session.GameSession;
import com.mmorpg.framework.packet.AbstractPacket;
import com.mmorpg.logic.base.login.packet.RespLoginAskPacket;
import com.mmorpg.framework.thread.god.IRequestToGod;
import com.mmorpg.framework.thread.god.Sprite;
import com.mmorpg.framework.thread.god.WorldScene;
import com.mmorpg.framework.thread.god.policy.IRequestOverflowPolicy;
import com.mmorpg.framework.thread.god.policy.impl.DirectRunPolicy;
import com.mmorpg.logic.base.scene.creature.player.Player;

/**
 * 门卫
 * 处理玩家登录和注册的服务
 */
public class GateKeepers extends Sprite {

	private IRequestOverflowPolicy policy = new DirectRunPolicy();

	private LoginWatcher watcher = LoginWatcher.getInstance();
	private int index;

	private GateKeepers(int index) {
		this.index = index;
	}

	private static int num = Constant.TWICE_CPU;
	private static GateKeepers[] keepers;

	public static void initialize() {
		keepers = new GateKeepers[num];
		for (int i = 0; i < num; i++) {
			keepers[i] = new GateKeepers(i);
			WorldScene.getInstance().executeWorker(keepers[i]);
		}
	}

	private static int hashIndex(GameSession session) {
		int index = 0;
		if (session.getAccount() == null) {
			index = (int) (session.getIpHashCode() % num);
		} else {
			index = session.getAccount().hashCode() % num;
		}
		return Math.abs(index);
	}

	public static void requestEnterWorld(GameSession session, AbstractPacket packet) {
		int index = hashIndex(session);
		keepers[Math.abs(index)].requestEnterWorld0(session, packet);
	}

	public static void requestExitworld(GameSession session) {
		int index = hashIndex(session);
		keepers[index].requestExitWorld0(session);
	}

	public static void execute(GameSession session, IRequestToGod request) {
		int index = hashIndex(session);
		keepers[Math.abs(index)].add(request);
	}

	public static final void processLogin(Player player, GameSession session, RespLoginAskPacket packet) {
		int index = hashIndex(session);
		keepers[Math.abs(index)].processLogin0(player, session, packet);
	}

	public void processLogin0(Player player, GameSession session, RespLoginAskPacket packet) {
		this.watcher.processLogin(player, session, packet);
	}

	/**
	 * 请求进入游戏世界
	 */
	private void requestEnterWorld0(GameSession session, AbstractPacket packet) {
		add(new RequestEnterWorld(session, packet));
	}

	/**
	 * 退出了过来打声招呼
	 */
	private void requestExitWorld0(GameSession session) {
		add(new RequestExitWorld(session));
	}

	@Override
	public void execute(boolean running) {
		watcher.heartbeat();
		super.execute(running);
	}

	@Override
	public String getName() {
		return "GateKeepers[" + index + "]";
	}

	@Override
	public IRequestOverflowPolicy getOverflowPolicy() {
		return policy;
	}

	@Override
	public int getMaxRequestNum() {
		return 8000;
	}

	@Override
	public int getMinSleepMills() {
		return 10;
	}

	@Override
	public int getProcessPeriod() {
		return 0;
	}

	@Override
	public int getProcessNumPerPeriod() {
		return 100;
	}
}
