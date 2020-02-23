package com.mmorpg.framework.packet.anno;

import java.lang.annotation.*;

/**
 * @author Ariescat
 * @version 2020/2/23 10:50
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Packet {

	short[] commandId();

	boolean cross() default false;
}

