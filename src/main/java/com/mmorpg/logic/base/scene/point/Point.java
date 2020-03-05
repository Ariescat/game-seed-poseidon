package com.mmorpg.logic.base.scene.point;

import lombok.Getter;

/**
 * 坐标点，不可变，当执行点里面的运算之后，返回一个新的点对象
 *
 * @author Ariescat
 * @version 2020/3/3 14:40
 */
@Getter
public class Point {

	private final short x;
	private final short y;

	public Point(short x, short y) {
		this.x = x;
		this.y = y;
	}
}
