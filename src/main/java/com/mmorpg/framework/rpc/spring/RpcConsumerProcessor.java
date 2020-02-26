package com.mmorpg.framework.rpc.spring;

import com.mmorpg.framework.annoscan.AnnoScannerListener;
import com.mmorpg.framework.rpc.anno.RpcConsumer;
import com.mmorpg.framework.rpc.anno.RpcMethod;
import com.mmorpg.framework.rpc.proxy.MethodCache;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author Ariescat
 * @version 2020/2/26 9:55
 * @see ScriptFactoryPostProcessor#setBeanFactory 注意这里复制了一次beanFactory
 */
@Component
public class RpcConsumerProcessor implements PriorityOrdered, BeanPostProcessor, ApplicationContextAware,
	BeanDefinitionRegistryPostProcessor, AnnoScannerListener {

	private ApplicationContext ctx;

	/**
	 * 扫描 {@link RpcMethod} 注解方法，并缓存
	 */
	@Override
	public void processResource(CachingMetadataReaderFactory factory, Resource[] resources) throws Exception {
		for (Resource resource : resources) {
			if (resource == null) continue;

			MetadataReader metadataReader = factory.getMetadataReader(resource);
			if (metadataReader.getClassMetadata().isInterface()) {
				if (metadataReader.getAnnotationMetadata().hasAnnotatedMethods(RpcMethod.class.getName())) {
					String className = metadataReader.getClassMetadata().getClassName();
					Class<?> clazz = Class.forName(className);
					MethodCache.cacheMethods(clazz);
				}
			}
		}
	}

	/**
	 * 扫描 {@link RpcConsumer} 注解字段，注入代理实现类
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> objClz = bean.getClass();
		for (Field field : objClz.getDeclaredFields()) {
			RpcConsumer annotation = field.getAnnotation(RpcConsumer.class);
			if (annotation != null) {
				Class<?> type = field.getType();
				field.setAccessible(true);

				try {
					Object proxy = RpcConsumers.get(type);
					field.set(bean, proxy);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} finally {
					field.setAccessible(false);
				}
			}
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 想法是 用Spring的 {@link Autowired} 替换 {@link RpcConsumer}， 也就是替换上面的 {@link #postProcessBeforeInitialization}
	 * 暂时不这样做先
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//		try {
//			List<Class<?>> classes = PacketSacnner.loadAllRpcInterfaceClassBySpring("com.mmorpg");
//			for (Class<?> clazz : classes) {
//				BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
//				GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
//				definition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
//				definition.setBeanClass(RpcConsumerFactoryBean.class);
//				definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
//				registry.registerBeanDefinition(clazz.getSimpleName(), definition);
//			}
//		} catch (BeanDefinitionStoreException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

}
