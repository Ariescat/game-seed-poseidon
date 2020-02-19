package com.mmorpg.logic.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ariescat
 * @version 2020/2/19 0:02
 */
public class ExceptionUtils {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);

    public static void log(Throwable t) {
        logger.info("", t);
    }

}
