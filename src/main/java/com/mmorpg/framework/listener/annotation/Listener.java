package com.mmorpg.framework.listener.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Inherited
@Documented
@Component
public @interface Listener {
	
	public final static int MIN_PRIORITY = 1;
	public final static int MINOR_PRIORITY = 3;
	public final static int NORM_PRIORITY = 5;
	public final static int MAX_PRIORITY = 7;

	int index() default NORM_PRIORITY;		// 顺序，数字越大，排到越前
}
