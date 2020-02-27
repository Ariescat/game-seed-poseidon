package com.mmorpg.logic.base.scene.creature;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ariescat
 * @version 2020/2/27 15:24
 */
public class GObject {

	protected final static Logger log = LoggerFactory.getLogger(GObject.class);

	/**
	 * 对象ID，场景上的每个对象具有唯一ID
	 */
	@Getter
	@Setter
	protected long id;
}
