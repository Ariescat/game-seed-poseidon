package com.mmorpg.framework.asyncdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Ariescat
 * @version 2020/2/26 15:19
 */
@Component
public class DefaultExceptionCallback implements ExceptionCallback {

	private final static Logger log = LoggerFactory.getLogger(DefaultExceptionCallback.class);

	@Override
	public void onException(Exception ex) {
		// TODO http上报
		ex.printStackTrace();
	}
}
