package com.mmorpg.framework.net.session;

/**
 * @author Ariescat
 * @version 2020/3/3 9:41
 */
public enum EClientType {

	Normal("1", "页面登录"),
	Mini_37("2", "37微端"),
	Box_37("3", "37盒子"),
	Sogou_Mini("4", "XX游戏大厅"),
	Sogou_Skin("5", "XX皮肤"),
	Mini_2144("6", "2144微端");

	private String key;
	private String name;

	EClientType(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public boolean typeOK(String key) {
		return this.key.equals(key);
	}
}
