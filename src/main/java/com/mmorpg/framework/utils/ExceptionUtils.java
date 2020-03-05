package com.mmorpg.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ariescat
 * @version 2020/2/19 0:02
 */
public class ExceptionUtils {

	private final static Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

	public static void error(Throwable var1) {
		log.error("", var1);
	}

	public static void error(String var1, Throwable var2) {
		log.error(var1, var2);
	}

	public static void error(String var1) {
		log.error(var1);
	}

	public static void error(String var1, Object var2) {
		log.error(var1, var2);
	}

	public static void error(String var1, Object var2, Object var3) {
		log.error(var1, var2, var3);
	}

	public static void error(String var1, Object... var2) {
		log.error(var1, var2);
	}


	private static StackTraceElement getCaller() {
		return Thread.currentThread().getStackTrace()[3];
	}

	@Deprecated
	public static void log(Throwable t) {
		log.error("Caller:{}\n", getCaller(), t);
	}

	@Deprecated
	public static void log(Throwable t, String msg) {
		log.error("Caller:{}\n{}", getCaller(), msg, t);
	}

	@Deprecated
	public static void log(Throwable t, String msg, Object... args) {
		log.error("Caller:{}\n{}", getCaller(), String.format(msg, args), t);
	}

	@Deprecated
	public static void log(String msg) {
		log.error("Caller:{}\n{}", getCaller(), msg);
	}

	@Deprecated
	public static void log(String msg, Object... args) {
		Object[] dest = new Object[args.length + 1];
		System.arraycopy(args, 0, dest, 1, args.length);
		dest[0] = getCaller();
		log.error("Caller:{}\n" + msg, dest);
	}

}
