package com.mmorpg.logic.base;

import com.mmorpg.framework.asyncdb.AsyncDBService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

public class Context implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static AsyncDBService asyncDBService;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public final static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public final static <T> Collection<T> getBeansOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz).values();
    }

    public final static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    @Autowired
    public void setAsyncDBService(AsyncDBService asyncDBService) {
        Context.asyncDBService = asyncDBService;
    }

    public static AsyncDBService getAsyncDBService() {
        return Context.asyncDBService;
    }
}
