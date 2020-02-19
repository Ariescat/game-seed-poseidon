package com.mmorpg.logic.base.business;

/**
 * 基础业务服务
 * 将场景无关的业务逻辑执行过程从场景线程剥离
 *
 * @author Ariescat
 * @version 2020/2/18 23:53
 */
public interface IBusinessService extends Runnable {


    /**
     * 线程名称
     */
    String getName();

    /**
     * 获取服务运行线程
     */
    Thread getThread();

    /**
     * 服务启动
     */
    void start();

    /**
     * 服务停止
     */
    void stop();

    /**
     * 每个周期最多处理多少个消息
     */
    int getProcessNumPerPeriod();

    /**
     * 推送消息
     *
     * @rsturn
     */
    boolean pushMessage(IBusinessMessage message);

    /**
     * 每个周期执行次
     */
    public void execute();

}
