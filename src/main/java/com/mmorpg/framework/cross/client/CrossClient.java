package com.mmorpg.framework.cross.client;

import com.mmorpg.framework.cross.DispatchPacket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @author Ariescat
 * @version 2019/12/24 10:21
 */
public class CrossClient implements Runnable {

	private BlockingQueue<DispatchPacket> packetQueue = new LinkedTransferQueue<>();

	@Override
	public void run() {

	}

	public boolean sendPacket(DispatchPacket packet) {
		return packetQueue.add(packet);
	}
}
