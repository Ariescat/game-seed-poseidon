package com.mmorpg.logic.base.scene;

import com.mmorpg.framework.thread.god.UniversalSpokesmanOfGod;
import com.mmorpg.framework.utils.ExceptionUtils;
import com.mmorpg.framework.utils.Profile;
import com.mmorpg.logic.base.scene.creature.GObject;
import com.mmorpg.logic.base.scene.creature.player.Player;
import com.mmorpg.logic.base.scene.point.Point;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ariescat
 * @version 2020/2/18 23:14
 */
public class Scene extends UniversalSpokesmanOfGod implements Cloneable {

	private final static Logger log = LoggerFactory.getLogger(Scene.class);

	/**
	 * 一个tick的时间
	 */
	private final static int TICK_TIME = 33;

	/**
	 * 每个tick处理多少个场景消息
	 */
	private final static int MESSAGE_PRE_TICK = 50;

	/**
	 * 最少休眠时间
	 */
	private final static int MIN_SLEEP_TIME = 5;

	/**
	 * 最小怪物自增ID
	 */
	private final static int MIN_MONSTER_AUTO_ID = 1000000;

	/**
	 * 怪物自增ID生成器
	 */
	private final AtomicLong monsterId = new AtomicLong(MIN_MONSTER_AUTO_ID);

	/**
	 * 当前场景ID
	 */
	private int sceneId;
	/**
	 * 场景是否退出
	 */
	private volatile boolean isExiting = false;
	/**
	 * 当前场景分线
	 */
	private int lineId;

	/**
	 * 场景消息
	 */
	private ConcurrentLinkedQueue<ISceneMessage> sceneMessages = new ConcurrentLinkedQueue<>();

	private StopWatch tickWatch = new StopWatch();

	private void preTick() {
		tickWatch.reset();
		tickWatch.start();
	}

	private void afterTick(TickType type) {
		tickWatch.stop();
		Profile.logTooLong(type, this, tickWatch.getTime());
	}

	private void tick(long utime) {
		preTick();
		// 处理场景消息
		processSceneMessage();
		afterTick(TickType.MESSAGE);

		preTick();
		processAllMove(utime);
		afterTick(TickType.MOVE);

		preTick();
		// 处理玩家
		processPlayers(utime);
		afterTick(TickType.PLAYER);

		preTick();
		// 处理怪物
		processMonster(utime);
		afterTick(TickType.PLAYER);

		preTick();
		processNPC(utime);
		afterTick(TickType.NPC);

		preTick();
		// 处理掉落物
		processSceneItem(utime);
		afterTick(TickType.DROP_ITEM);

		preTick();
		// 处理地图buff TODO
		afterTick(TickType.SCENE_BUFF);

		checkStop();
	}

	public void addSceneMessage(ISceneMessage message) {
		if (isExiting) {
			log.error("Scene is exiting");
		}
		sceneMessages.add(message);
	}

	/**
	 * 处理场景消息
	 */
	private void processSceneMessage() {
		try {
			ISceneMessage message;
			int count = 0;
			while (count < MESSAGE_PRE_TICK && (message = this.sceneMessages.poll()) != null) {
				message.execute(this);
				count++;
			}
		} catch (Exception e) {
			ExceptionUtils.error("{} processSceneMessage", this, e);
		}
	}

	private void processAllMove(long utime) {

	}

	private void processPlayers(long utime) {

	}

	private void processMonster(long utime) {

	}

	private void processNPC(long utime) {
		processFighterNPC(utime);

		// 处理采集NPC
		processCollectNPC(utime);

		processRefreshNPC(utime);
	}

	private void processFighterNPC(long utime) {

	}

	private void processCollectNPC(long utime) {

	}

	private void processRefreshNPC(long utime) {

	}

	private void processSceneItem(long utime) {

	}

	private void checkStop() {

	}

	public void enterScene(Player player, Point point) {
		// TODO 打断移动
		addSceneMessage(new RequestEnterScene(player, sceneId, point));
	}

	public void objectEnterScene(GObject object) {
		// TODO 视野同步
	}

	public void beforeObjectEnterScene(GObject object) {
		// TODO 提供生命周期流程
	}

	public void afterObjectEnterScene(GObject object) {

	}

	public void objectLeaveScene(GObject object) {
		// TODO 视野同步
	}

	public void beforeObjectLeaveScene(GObject object) {

	}

	public void afterObjectLeaveScene(GObject object) {

	}

	@Override
	public int getMinSleepMills() {
		return MIN_SLEEP_TIME;
	}

	@Override
	public int getProcessPeriod() {
		return MESSAGE_PRE_TICK;
	}

	@Override
	public void execute(boolean running) {
		super.updateLastExecuteTimeMillis();
		tick(super.getLastExecuteTimeMillis());
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public String toString() {
		return "Scene [" + sceneId + "-" + lineId + "]";
	}
}
