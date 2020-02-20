package com.mmorpg.framework.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CoreThreadFactory implements ThreadFactory {

	private final ThreadGroup group;
	private final AtomicInteger threadNumber;
	private final String namePrefix;
	private final boolean daemon;

	public CoreThreadFactory(String namePrefix) {
		this(namePrefix, false);
	}

	public CoreThreadFactory(String namePrefix, boolean daemon) {
		threadNumber = new AtomicInteger(1);
		SecurityManager localSecurityManager = System.getSecurityManager();
		group = localSecurityManager == null
			? Thread.currentThread().getThreadGroup()
			: localSecurityManager.getThreadGroup();
		this.namePrefix = namePrefix + "-" + "-thread-";
		this.daemon = daemon;
	}

	@Override
	public Thread newThread(Runnable paramRunnable) {
		Thread localThread = new Thread(group, paramRunnable,
			String.valueOf(namePrefix) + threadNumber.getAndIncrement(),
			0L);
		if (localThread.isDaemon()) {
			localThread.setDaemon(daemon);
		}
		if (localThread.getPriority() != Thread.NORM_PRIORITY) {
			localThread.setPriority(Thread.NORM_PRIORITY);
		}
		return localThread;
	}
}
