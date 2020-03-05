package com.mmorpg.logic.base.scene.creature;

import com.mmorpg.logic.base.scene.Scene;
import com.mmorpg.logic.base.scene.point.Point;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ariescat
 * @version 2020/2/27 15:24
 */
@Getter
@Setter
public class GObject {

	protected final static Logger log = LoggerFactory.getLogger(GObject.class);

	/**
	 * 对象ID，场景上的每个对象具有唯一ID
	 */
	protected long id;

	/**
	 * 所在场景ID
	 */
	private int sceneId;

	/**
	 * 所在场景引用
	 */
	private volatile Scene scene;

	/**
	 * 当前点
	 */
	private Point coord;

	private float coordx;
	private float coordy;

}
