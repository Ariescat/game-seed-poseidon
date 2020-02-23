package com.mmorpg.framework.rpc.msg.repair;

import java.util.HashMap;
import java.util.Map;

/**
 * 临时修复数据包
 *
 * @author Ariescat
 * @version 2020/2/23 11:09
 */
public class TempRepairMsg {

	protected Map<String, Object> repairMap;

	public Map<String, Object> getRepairMap() {
		return repairMap;
	}

	public void putRepairItem(String key, Object value) {
		if (repairMap == null) {
			repairMap = new HashMap<>();
		}
		repairMap.put(key, value);
	}
}
