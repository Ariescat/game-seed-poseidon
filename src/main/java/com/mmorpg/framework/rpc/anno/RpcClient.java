package com.mmorpg.framework.rpc.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定 Rpc Client
 *
 * @author Ariescat
 * @version 2019/12/24 10:04
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcClient {

    String value();
}
