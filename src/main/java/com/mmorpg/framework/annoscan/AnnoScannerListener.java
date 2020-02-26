package com.mmorpg.framework.annoscan;

import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

/**
 * @author Ariescat
 * @version 2020/2/26 11:08
 */
public interface AnnoScannerListener {

	void processResource(CachingMetadataReaderFactory factory, Resource[] resources) throws Exception;
}
