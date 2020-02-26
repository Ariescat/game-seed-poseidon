package com.mmorpg.framework.groovy;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.File;
import java.net.URL;
import java.util.Collection;

public class GroovyFactory implements ApplicationContextAware {

	private final static Logger log = LoggerFactory.getLogger(GroovyFactory.class);

	/**
	 * 脚本在classpath下的目录
	 */
	private String directory;

	/**
	 * 检测脚本是否修改间隔
	 */
	private int refreshCheckDelay = 500;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		// 只有这个对象才能注册bean到spring容器
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();

		// 因为spring会自动将xml解析成BeanDefinition对象然后进行实例化，这里我们没有用xml，所以自己定义BeanDefinition
		// 这些信息跟spring配置文件的方式差不多，只不过有些东西lang:groovy标签帮我们完成了
		final String refreshCheckDelay = "org.springframework.scripting.support.ScriptFactoryPostProcessor.refreshCheckDelay";
		final String language = "org.springframework.scripting.support.ScriptFactoryPostProcessor.language";

		URL url = Thread.currentThread().getContextClassLoader().getResource(directory);
		if (url == null) {
			log.warn("groovy directory is null");
			return;
		}

		File scriptDir = new File(url.getFile());
		if (!scriptDir.exists()) return;

		Collection<File> files = FileUtils.listFiles(scriptDir, new String[]{"java", "groovy"}, true);
		for (File file : files) {
			GenericBeanDefinition bd = new GenericBeanDefinition();
			bd.setBeanClassName("org.springframework.scripting.groovy.GroovyScriptFactory");
			// 刷新时间
			bd.setAttribute(refreshCheckDelay, 500);
			// 语言脚本
			bd.setAttribute(language, "groovy");
			// 文件目录
			String scriptLocator = file.getPath().substring(file.getPath().indexOf(directory));
			bd.getConstructorArgumentValues().addIndexedArgumentValue(0, scriptLocator);
			// 注册到spring容器
			beanFactory.registerBeanDefinition(file.getName().replace(".groovy", ""), bd);

			log.info("register groovy script: {}", scriptLocator);
		}
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public int getRefreshCheckDelay() {
		return refreshCheckDelay;
	}

	public void setRefreshCheckDelay(int refreshCheckDelay) {
		this.refreshCheckDelay = refreshCheckDelay;
	}
}
