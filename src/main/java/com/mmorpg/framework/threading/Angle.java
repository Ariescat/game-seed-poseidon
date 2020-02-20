package com.mmorpg.framework.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Angle extends UniversalSpokesmanOfGod {

	private final static Logger log = LoggerFactory.getLogger(Angle.class);
	private final static Angle instance = new Angle();

	private ConcurrentLinkedQueue<ISpokesmanOfGod> services;

	private Angle() {
	}

	public static Angle getInstance() {
		return instance;
	}

	public void registerWorkers(ConcurrentLinkedQueue<ISpokesmanOfGod> services) {
		this.services = services;
	}

	@Override
	public void execute(boolean running) {
		for (ISpokesmanOfGod service : services) {
			IUndeadConfig undeadConfig = service.getUndeadConfig();
			if (undeadConfig != null && undeadConfig.isDead()) {
				StringBuilder info = new StringBuilder();

				if (log.isWarnEnabled()) {
					log.warn("Service[{}] has be checked died. date:{}", service.getName(), System.currentTimeMillis());
				}

				// before remove
				service.stop();
				WorldScene.getInstance().removeWorker(service);

				// after remove
				WorldScene.getInstance().executeWorker(service);
			}
		}
	}

	@Override
	public String getName() {
		return "Angle[Relife the UndeadService which has died]";
	}

	@Override
	public IUndeadConfig getUndeadConfig() {
		return null;
	}

	@Override
	public int getMinSleepMills() {
		return 1000;
	}

	@Override
	public int getProcessPeriod() {
		return 5000;
	}
}
