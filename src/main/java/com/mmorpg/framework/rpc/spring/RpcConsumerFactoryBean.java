package com.mmorpg.framework.rpc.spring;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Ariescat
 * @version 2020/2/26 11:46
 */
public class RpcConsumerFactoryBean<T> implements FactoryBean<T> {

	private Class<T> interfaceClass;

	public RpcConsumerFactoryBean(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	@Override
	public T getObject() throws Exception {
		//noinspection unchecked
		return (T) RpcConsumers.createRpcProxy(interfaceClass);
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
