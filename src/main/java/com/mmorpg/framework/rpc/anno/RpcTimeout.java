package com.mmorpg.framework.rpc.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ariescat
 * @version 2019/12/24 10:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcTimeout {

    /**
     * @return 重写TimeOut时间 毫秒mill
     */
    int value();
}
