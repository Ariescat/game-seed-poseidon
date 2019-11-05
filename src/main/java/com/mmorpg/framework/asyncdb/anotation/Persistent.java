package com.mmorpg.framework.asyncdb.anotation;

import com.mmorpg.framework.asyncdb.Synchronizer;

import java.lang.annotation.*;

/**
 * 持久化注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Persistent {

    /**
     * 是否是异步持久化
     */
    boolean asyn() default true;

    /**
     * 获取数据库同步器类型
     */
    Class<? extends Synchronizer> syncClass();
}
