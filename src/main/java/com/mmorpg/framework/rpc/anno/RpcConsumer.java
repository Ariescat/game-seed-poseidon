package com.mmorpg.framework.rpc.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法返回值为 Void 数据只会单向发送
 * 返回值为 RpcResponseFuture 可以直接得到 Future
 * 其他返回值会同步等待
 *
 * @author Ariescat
 * @version 2019/12/24 10:07
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcConsumer {
}
