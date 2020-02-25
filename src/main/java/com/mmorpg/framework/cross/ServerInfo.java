package com.mmorpg.framework.cross;

/**
 * @author Ariescat
 * @version 2020/2/25 17:16
 */
public class ServerInfo {

	private String platform;
	private int serverId;
	private String serverName;
	private String ip;
	private int port;

	private transient String ipPort;
	private transient String key;

	public ServerInfo() {
	}

	public ServerInfo(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public ServerInfo(String host) {
		String[] strings = host.split(":");
		this.ip = strings[0];
		this.port = Integer.parseInt(strings[1]);
	}

	public static String generateIpPort(String ip, int port) {
		return ip + ":" + port;
	}

	public String getIpPort() {
		if (ipPort == null) {
			ipPort = generateIpPort(ip, port);
		}
		return ipPort;
	}

	public static String generateKey(String platform, int serverId) {
		return platform + "_" + serverId;
	}

	public String getKey() {
		if (key == null) {
			key = generateKey(platform, serverId);
		}
		return key;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "ServerInfo{" +
			"platform='" + platform + '\'' +
			", serverId=" + serverId +
			", serverName='" + serverName + '\'' +
			", ip='" + ip + '\'' +
			", port=" + port +
			'}';
	}
}
