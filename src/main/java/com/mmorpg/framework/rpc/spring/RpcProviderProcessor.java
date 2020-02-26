package com.mmorpg.framework.rpc.spring;

import com.mmorpg.framework.rpc.anno.RpcProvider;
import com.mmorpg.framework.rpc.proxy.MethodCache;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/26 9:43
 */
@Component
public class RpcProviderProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> targetClass = AopUtils.getTargetClass(bean);
		RpcProvider annotation = targetClass.getAnnotation(RpcProvider.class);
		if (annotation != null) {
			Class<?> providerClazz = annotation.clazz();
			// 对接口的方法进行缓存
			MethodCache.cacheMethods(providerClazz);
			RpcProviders.addProvider(providerClazz.getName(), bean);
		}
		return bean;
	}
}
