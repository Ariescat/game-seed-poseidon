package com.mmorpg.framework.rpc.msg;

import com.mmorpg.framework.annoscan.AnnoScannerListener;
import com.mmorpg.framework.rpc.msg.anno.CrossMsg;
import org.apache.commons.collections.MapUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ariescat
 * @version 2020/2/26 13:20
 */
@Component
public class CrossMsgScanner implements AnnoScannerListener {

	@Override
	public void processResource(CachingMetadataReaderFactory factory, Resource[] resources) throws Exception {
		Map<Short, Class<? extends ICrossBaseMsg>> msgs = new HashMap<>();
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader reader = factory.getMetadataReader(resource);
				AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
				Map<String, Object> attr = annotationMetadata.getAnnotationAttributes(CrossMsg.class.getName());
				if (MapUtils.isNotEmpty(attr)) {
					String className = annotationMetadata.getClassName();
					Class<?> clazz = Class.forName(className);
					if (ICrossBaseMsg.class.isAssignableFrom(clazz)) {
						Short id = (Short) attr.get("value");
						//noinspection unchecked
						Class<? extends ICrossBaseMsg> old = msgs.put(id, (Class<? extends ICrossBaseMsg>) clazz);
						if (old != null) {
							throw new RuntimeException("CrossMsg ID 冲突：" + id + " " + clazz.getName() + " and " + old.getName());
						}
					}
				}
			}
		}
		CrossMsgFactory.init(msgs);
	}
}
