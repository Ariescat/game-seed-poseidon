package com.mmorpg.framework.cross;

/**
 * @author Ariescat
 * @version 2020/2/25 17:50
 */
public enum CrossType {

	DEVIL_ALTER(RemoteServers.DEVIL_ALTER),
	;

	private final RemoteServers servers;

	CrossType(RemoteServers servers) {
		this.servers = servers;
	}

	public RemoteServers getServers() {
		return servers;
	}
}
