package com.mmorpg.framework.rpc.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记远程调用方法唯一ID（全局） 用于减少方法调用时的数据包大小
 *
 * @author Ariescat
 * @version 2019/12/24 10:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcMethod {

	/**
	 * @return 远程方法调用 唯一ID 不能为0
	 */
	int value();
}
