package com.mmorpg.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ariescat
 * @version 2020/2/19 0:02
 */
public class ExceptionUtils {

	private final static Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

	private static StackTraceElement getCaller() {
		return Thread.currentThread().getStackTrace()[3];
	}

	public static void log(Throwable t) {
		log.error("Caller:{}\n", getCaller(), t);
	}

	public static void log(String msg, Object... args) {
		Object[] dest = new Object[args.length + 1];
		System.arraycopy(args, 0, dest, 1, args.length);
		dest[0] = getCaller();
		log.error("Caller:{}\n" + msg, dest);
	}
}
