package com.mmorpg.framework.asyncdb;

import com.mmorpg.logic.base.Context;

public class BaseDBEntity extends AsynDBEntity {

	public void insert() {
		if (isRobot() || isCross()) {
			return;
		}
		Context.it().asyncDBService.insert(this);
	}

	public void update() {
		if (isRobot()) {
			return;
		}
		Context.it().asyncDBService.update(this);
	}

	public void delete() {
		if (isRobot()) {
			return;
		}
		Context.it().asyncDBService.delete(this);
	}

	public void serialize() {

	}

	public void deserialize() {

	}

//    public ISaver saver() {
//        return new BaseDBEntitySaver(this);
//    }

	private boolean isCross() {
		return false;
	}

	public boolean isRobot() {
		return false;
	}
}
