package com.mmorpg.framework.net.session;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmorpg.framework.cross.CrossInfo;
import com.mmorpg.framework.net.NullChannel;
import com.mmorpg.framework.packet.AbstractPacket;
import com.mmorpg.framework.packet.PacketDecodeException;
import com.mmorpg.framework.packet.PacketFactory;
import com.mmorpg.framework.net.Response;
import com.mmorpg.framework.utils.*;
import com.mmorpg.framework.utils.random.RandomUtils;
import com.mmorpg.logic.base.Context;
import com.mmorpg.logic.base.domain.Player;
import com.mmorpg.logic.base.service.ConfigService;
import com.mmorpg.logic.base.cheat.ISerialNumberHandler;
import com.mmorpg.logic.base.cheat.PacketCheatLogEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 登陆监视器
 *
 * @author Ariescat
 * @version 2020/2/19 11:33
 */
public class GameSession {

	private final static Logger log = LoggerFactory.getLogger(GameSession.class);

	private static final AtomicReferenceFieldUpdater<GameSession, GameSessionStatus> StateUpdater
		= AtomicReferenceFieldUpdater.newUpdater(GameSession.class, GameSessionStatus.class, "status");

	private Channel channel;
	private String account = "";    //帐号
	private String account_server;
	private int salt;
	private int key;
	private int nextKey = -1;
	private long lastKeyResetTime = TimeUtils.getCurrentMillisTime();
	private int nextDelay;
	private int nextExpire;
	private long uid;    //用户ID
	private Player player;
	private volatile GameSessionStatus status = GameSessionStatus.INIT;
	private volatile boolean authed;
	private String ip;
	private String server;
	private long ipHashCode;
	/**
	 * 是否成年
	 */
	private boolean al;
	/**
	 * 登录类型
	 */
	private String client;
	/**
	 * 参数
	 */
	private Map<String, String> paramMap = Collections.emptyMap();
	private String params;
	/**
	 * 是否微端登录
	 */
	private boolean isClient;
	/**
	 * 是否37盒子登录
	 */
	private boolean is37Box;
	/**
	 * 是否搜狗游戏平台登陆
	 */
	private boolean isSogouMini;
	/**
	 * 是否搜狗皮肤登陆
	 */
	private boolean isSogouSkin;
	/**
	 * 是否2345王牌浏览器登陆
	 */
	private boolean is2345Login = false;
	/**
	 * 平台
	 */
	private String platform;
	/**
	 * 序列号
	 */
	private final AtomicInteger serialNO = new AtomicInteger(0);
	private ISerialNumberHandler serialNumberHandler;
	private int serialNumberRandom;
	private ISerialNumberHandler nextSerialNumberHandler;
	private int nextSerrialNumberRandom;

	/**
	 * 上次检测收包数量的时间
	 */
	private long lastCheckTime;
	/**
	 * 是否正在退出标志位
	 */
	private AtomicBoolean exiting = new AtomicBoolean();

	private boolean registered;
	/**
	 * 平台vip等级
	 **/
	private int platVipLv;

	/**
	 * 中37平台完善资料
	 */
	private boolean userInfo37;
	/**
	 * 包ID->数量
	 */
	private Map<Short, Integer> packetId2Count = Maps.newHashMap();
	/**
	 * 二次密码是否正确
	 */
	private boolean securityPass;
	/**
	 * 是否从后台登陆
	 */
	private boolean backend;
	/**
	 * 是否绑定令牌
	 */
	private boolean lingpai;
	/**
	 * 渠道礼包序列码
	 */
	private String InoCode;

	private short lastHeartbeatRandom = 0;
	private String channelValue;
	private Map<String, Object> unknowArgs;
	private long encryptTimeKey = -1;
	private boolean firstCheatCheck = true;
	private long enterdSceneTime;
	private boolean baidyTGHW;
	private boolean hao123;
	/**
	 * 注册渠道
	 */
	private String referCode;

	private String userRefer;

	private String fdata_ext;

	private boolean creatRoleCheatPacket = false;//是否 收到防外挂，创角协议
	private boolean loginCheatPacket = false;//是否 收到防外挂，登陆协议
	/**
	 * 版本是否通过验证
	 */
	private boolean versionValid;

	public GameSession(Channel channel) {
		this.channel = channel;
		this.ip = ChannelUtils.getIP(channel);
		this.ipHashCode = IpAddressUtils.hashCode(ip, ((InetSocketAddress) channel.remoteAddress()).getPort());
		this.authed = false;
	}

	private GameSession(String ip, int port) {
		this.channel = new NullChannel();
		this.ip = ip;
		this.ipHashCode = IpAddressUtils.hashCode(ip, port);
		this.authed = true;
	}

	public static GameSession createSysGameSession(Player player) {
		GameSession session = new GameSession("127.0.0.1", 5051);
		session.setAccount(player.getAccount());
		session.setStatus(GameSessionStatus.ENTERING_SCENE);
		session.setServer(Context.it().configService.getOriServerFlag());
		session.genASKey();
		return session;
	}

	public long getIpHashCode() {
		return ipHashCode;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void updateAccount(String account) {
		this.account = account;
		this.genASKey();
	}

	public long getUid() {
		return uid;
	}

	public void sendPacket(AbstractPacket packet) {
		if (packet == null) {
			return;
		}
		Response response = packet.write();
		if (response == null) {
			return;
		}
		write(response);
	}

	public boolean write(Response response) {
		if (channel instanceof NullChannel) {
			return false;
		}
		if (response != null) {
			if (Context.it().configService.isFlushStatus()) {
				channel.writeAndFlush(response);
			} else {
				ChannelFuture writeFuture = channel.write(response);
				scheduleFlush(writeFuture);
			}
			return true;
		}
		return false;
	}

	private void scheduleFlush(final ChannelFuture writeFuture) {
		final ScheduledFuture<?> sf = channel.eventLoop().schedule(new Runnable() {
			@Override
			public void run() {
				if (!writeFuture.isDone()) {
					channel.flush();
				}
			}
		}, 30, TimeUnit.MILLISECONDS);

		// Cancel the scheduled timeout if the flush future is complete.
		writeFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				sf.cancel(false);
			}
		});
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player, GameSessionStatusUpdateCause cause) {
		this.player = player;
		this.uid = player.getId();
		player.setSession(this, cause);
	}

	public GameSessionStatus getStatus() {
		return status;
	}

	public void setStatus(GameSessionStatus status) {
		this.status = status;
	}

	public boolean compareAndSetStatus(GameSessionStatus expect, GameSessionStatus update) {
		return StateUpdater.compareAndSet(this, expect, update);
	}

	/**
	 * 能否把包交给场景处理
	 */
	public boolean isCanProcessPacketInScene() {
		return status == GameSessionStatus.ENTERING_SCENE ||
			status == GameSessionStatus.ENTERED_SCENE;
	}

	public void close(CloseCause cause, String... ip) {
		try {
			if (channel.isOpen()) {
				channel.close();
				log.info("CloseSession[{}] due to Cause[{}] {}", getAccount(), cause, ip);
			} else {
				log.info("AlreadyCloseSession[{}] due to Cause[{}] {}", getAccount(), cause, ip);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getIP() {
		return ip;
	}

	/**
	 * 序列号是否合法 等于或者大于服务端 当前值为合法，如果大于服务器当前值，则将服务器端当前值设置为客户端的
	 */
	public void checkAvailableSerialNo(int recvSerialNo, boolean needDecode) throws PacketDecodeException {
		if (!needDecode) {
			return;
		}
		if (serialNO.get() >= Short.MAX_VALUE) {
			serialNO.compareAndSet(serialNO.get(), 0);
		}
		int current = serialNO.incrementAndGet();
		int sno = serialNumberHandler.handle(recvSerialNo, salt, serialNumberRandom);
		if (sno != current) {
			log.error("SNOMeta {} {} recvSerisNO:{} salt:{} serialNumberRandom:{} current:{} sno:{}",
				account, serialNumberHandler.getClass().getSimpleName(), recvSerialNo, salt, serialNumberRandom, current, sno);
			Context.it().eventBus.post(new PacketCheatLogEvent(account, server, 1));
			CheatUtils.sendCheatAndClose(channel);
		}
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public boolean needCheck() {
		return true;
	}

	public boolean isAuthed() {
		return authed;
	}

	public void authed() {
		this.authed = true;
		this.genASKey();
	}

	public void setAl(boolean al) {
		this.al = al;
	}

	/**
	 * 是否成年
	 */
	public boolean isA1() {
		return this.al;
	}

	/**
	 * 返回false为未通过检测
	 */
	public boolean checkFrequency(AbstractPacket packet) {
		final ConfigService configService = Context.it().configService;
		long curTime = TimeUtils.getCurrentMillisTime();
		if (curTime - lastCheckTime > configService.getPlugInterval()) {
			lastCheckTime = curTime;
			this.packetId2Count.clear();
		}
		Short packetId = packet.getCommand();
		Integer count = packetId2Count.get(packetId);
		if (count == null) {
			count = 0;
			packetId2Count.put(packetId, count);
		}
		count++;
		if (count > configService.getPlugThreshold()) {
			List<String> msgs = Lists.newLinkedList();
			for (Map.Entry<Short, Integer> entry : packetId2Count.entrySet()) {
				msgs.add(entry.toString());
			}
			String msg = Joiner.on(",").join(msgs);
			log.error("FrequencyCheckFailed {} {} {}", ip, account, msg);
		}
		packetId2Count.put(packetId, count);
		return true;
	}

	public boolean isInit() {
		return this.status == GameSessionStatus.INIT;
	}

	public boolean isLoginAuth() {
		return this.status == GameSessionStatus.LOGIN_AUTH;
	}

	public boolean isEnteringScene() {
		return this.status == GameSessionStatus.ENTERING_SCENE;
	}

	public boolean isEnteredScene() {
		return this.status == GameSessionStatus.ENTERED_SCENE;
	}

	/**
	 * 是否正在退出中
	 */
	public boolean isLogouting() {
		return this.status == GameSessionStatus.LOGOUTING;
	}

	public boolean setExiting() {
		return this.exiting.compareAndSet(false, true);
	}

	public boolean isExiting() {
		return this.exiting.get();
	}

	public boolean isRegistered() {
		return registered;
	}

	public void registered() {
		this.registered = true;
	}

	public int generateKey() {
		if (PacketFactory.isPacketEncryptEnabled()) {
			int key = AuthUtils.genKey();
			this.key = key;
			this.salt = AuthUtils.restoreSalt(key);
			return key;
		}
		this.salt = 0;
		return 0;
	}

	public int reGenerateKey(int nextDelay, int nextExpire) {
		if (PacketFactory.isPacketEncryptEnabled()) {
			int key = AuthUtils.genKey();
			this.nextKey = key;
			this.resetKeyTime(nextDelay, nextExpire);
			return key;
		}
		this.salt = 0;
		return 0;
	}

	public void resetKeyTime(int nextDelay, int nextExpire) {
		this.lastKeyResetTime = TimeUtils.getCurrentMillisTime();
		this.nextDelay = nextDelay;
		this.nextExpire = nextExpire;
	}

	public void confirmGenerateKey() {
		if (PacketFactory.isPacketEncryptEnabled() && this.nextKey != -1) {
			this.key = this.nextKey;
			this.salt = AuthUtils.restoreSalt(key);
			this.nextKey = -1;
		}
	}

	public int getKey() {
		return key;
	}

	public int randomStartSNO() {
		int sno = RandomUtils.nextInt(Short.MAX_VALUE);
		this.serialNO.set(sno);
		return sno;
	}

	public int getSerialN0() {
		return this.serialNO.get();
	}

	public int getSalt() {
		return salt;
	}

	public boolean isClient() {
		return isClient;
	}

	/**
	 * 是否37盒子登陆
	 */
	public boolean is37Box() {
		return is37Box;
	}

	/**
	 * 是否搜狗游戏大厅登陆
	 */
	public boolean isSogouMini() {
		return isSogouMini;
	}

	/**
	 * 是否搜狗皮肤登陆
	 */
	public boolean isSogouSkin() {
		return isSogouSkin;
	}

	public boolean is2345Login() {
		return is2345Login;
	}

	public void set2345Login(boolean is2345Login) {
		this.is2345Login = is2345Login;
	}

	public String getPlatforn() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
		if (StringUtils.isNotBlank(client)) {
//            this.isClient = EClientType.Mini_37.type0k(client) || EClientType.Mini_2144.type0k(client);
//            this.is37Box - EClientType.Box_37.type0k(client);
//            this.isSogouMini - EClientType.SogouMini.typeOk(client);
//            this.isSogouSkin = EClientType.SogouSkin.typeOk(client);
		}
	}

	public String getAServer() {
		return account_server;
	}

	private void genASKey() {
//        this.account_server =
	}

	public boolean isCross() {
		if (player == null) {
			return false;
		}
		CrossInfo crossInfo = player.getCrossInfo();
		return crossInfo.isCrossed();
	}

	public CrossInfo getCrossInfo() {
		if (player == null) {
			return null;
		}
		return player.getCrossInfo();
	}

	@Override
	public String toString() {
		return super.toString() + " " + status + " " + account;
	}


}

