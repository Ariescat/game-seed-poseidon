package com.mmorpg.framework.annoscan;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Ariescat
 * @version 2020/2/26 10:25
 */
@Component
public class AnnoScanner implements InitializingBean, ApplicationContextAware {
	private final static String scanPackage = "com.mmorpg";
	private ApplicationContext ctx;

	@Override
	public void afterPropertiesSet() throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory(resolver);
		Resource[] resources = resolver.getResources("classpath*:" + scanPackage.replace('.', '/') + "/**/*.class");
		Map<String, AnnoScannerListener> beansOfType = ctx.getBeansOfType(AnnoScannerListener.class);
		if (beansOfType != null) {
			for (AnnoScannerListener listener : beansOfType.values()) {
				listener.processResource(factory, resources);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
}
