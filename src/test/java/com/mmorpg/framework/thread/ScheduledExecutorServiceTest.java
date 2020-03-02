package com.mmorpg.framework.thread;

import com.mmorpg.framework.thread.threads.SimpleThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步线程例子
 */
public class ScheduledExecutorServiceTest {

	private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new SimpleThreadFactory("ScheduledExecutorServiceTest"));

	/**
	 * 异步起一条线程每秒处理3次请求执行周期任务
	 *
	 * @param time
	 */
	private void schedule(final int time) {
		if (time > 3) {
			return;
		}
		//schedule方法被用来延迟指定时间后执行某个指定任务
		executorService.schedule(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}

//				time++;
			}
		}, time - 1, TimeUnit.SECONDS);
	}

	private void submit() {
		//submit方法有返回值Future。get()
		executorService.submit(new Runnable() {
			@Override
			public void run() {

			}
		});
	}
}
