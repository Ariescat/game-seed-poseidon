package com.mmorpg.logic.base.scene.creature.player.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ariescat
 * @version 2020/2/27 15:34
 */
public class PlayerEntity {

	@Getter
	@Setter
	private long playerId;

	@Getter
	@Setter
	private String account;

	@Getter
	@Setter
	private String server;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private byte roleType; // 角色类型
}
