package com.mmorpg.framework.asyncdb;

import com.mmorpg.framework.asyncdb.anotation.Persistent;
import com.mmorpg.framework.server.ICloseEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AsyncDBService implements ApplicationContextAware, ICloseEvent {

    /**
     * 队列数量
     */
    private int threads = 32;
    private int step = 10;
    private int speed = 10;
    private int tryTime = 10;

    @Autowired
    private ExceptionCallback callback;

    @Autowired
    private ISyncStrategy syncStrategy;

    private SyncDBExecutor syncDBExecutor;

    private final Map<Class<?>, Synchronizer> synchronizerMap = new ConcurrentHashMap<Class<?>, Synchronizer>();

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Map<String, Synchronizer> map = context.getBeansOfType(Synchronizer.class);
        for (Synchronizer synchronizer : map.values()) {
            synchronizerMap.put(synchronizer.getClass(), synchronizer);
        }
    }

    @PostConstruct
    public void init() {
        Objects.requireNonNull(callback);
        syncDBExecutor = new SyncDBExecutor(callback, threads, step, speed, tryTime);
    }

    public void delete(AsynDBEntity entity) {
//        entity.serialize();
        this.doOperation(entity, Operation.DELETE);
    }

    public void update(AsynDBEntity entity) {
        entity.serialize();
        this.doOperation(entity, Operation.UPDATE);
    }

    public void insert(AsynDBEntity entity) {
        entity.serialize();
        this.doOperation(entity, Operation.INSERT);
    }

    private boolean doOperation(AsynDBEntity entity, Operation operation) {
        boolean sumit = false;
        sumit = entity.submit(operation);
        if (sumit) {
            if (entity.getSynchronizer() == null) {
                entity.setSynchronizer(this.synchronizerMap.get(getPersistentClass(entity.getClass())));
            }
            return this.syncDBExecutor.submit(entity);
        }
        return true;
    }

    private Class<?> getPersistentClass(Class<?> entityClass) {
        Persistent persistent = entityClass.getAnnotation(Persistent.class);
        return persistent.syncClass();
    }

    public boolean stop() {
        try {
            return this.syncDBExecutor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onServerClose() {
        stop();
    }
}
