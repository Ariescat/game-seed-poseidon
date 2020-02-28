package com.mmorpg.logic.modules.activity.dao;

import com.mmorpg.logic.modules.activity.entity.PlayerActivity;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

/**
 * @author Ariescat
 * @version 2020/2/28 15:33
 */
@DAO
public interface PlayerActivityDAO {

	String TABLE = "PlayerActivity";
	String FIELDS = "playerId, data";

	@SQL("SELECT " + FIELDS + " FROM" + TABLE + " WHERE playerId=:1")
	PlayerActivity select(long playerId);
}
