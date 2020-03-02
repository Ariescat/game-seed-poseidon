package com.mmorpg.framework.packet;

import com.koloboke.collect.map.ShortObjMap;
import com.koloboke.collect.map.hash.HashShortObjMaps;
import com.mmorpg.framework.annoscan.AnnoScannerListener;
import com.mmorpg.framework.packet.anno.Packet;
import org.apache.commons.collections.MapUtils;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Map;

/**
 * @author Ariescat
 * @version 2020/3/2 10:46
 */
@Component
public class PacketScanner implements AnnoScannerListener, ApplicationContextAware {

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

	@Override
	public void processResource(CachingMetadataReaderFactory factory, Resource[] resources) throws Exception {

		final ShortObjMap<Class<? extends AbstractPacket>> packets = HashShortObjMaps.newUpdatableMap();
		final ShortObjMap<Boolean> crossPackets = HashShortObjMaps.newUpdatableMap();

		loadPacketByResourceScan(factory, resources, new Consumer() {
			@Override
			public void accept(Class<? extends AbstractPacket> clazz, short[] commandIds, boolean cross) {
				doLoadPacket(clazz, commandIds, cross, packets, crossPackets);
			}
		});

		PacketFactory.init(packets, crossPackets);
	}

	private void doLoadPacket(Class<? extends AbstractPacket> clazz, short[] commandIds, boolean cross, ShortObjMap<Class<? extends AbstractPacket>> packets, ShortObjMap<Boolean> crossPackets) {
		for (short commandId : commandIds) {
			if (packets.containsKey(commandId)) {
				//noinspection ConstantConditions
				throw new RuntimeException("conflict packet id [" + commandId + "]"
					+ clazz.getName()
					+ " and "
					+ packets.get(commandId).getName());
			}
			packets.put(commandId, clazz);
			if (cross) {
				crossPackets.put(commandId, Boolean.TRUE);
			}
		}
	}

	private void loadPacketOld(Consumer consumer) {
		Map<String, Object> beans = ctx.getBeansWithAnnotation(Packet.class);
		for (Object bean : beans.values()) {
			AbstractPacket packet = (AbstractPacket) bean;
			Packet annotation = packet.getClass().getAnnotation(Packet.class);
			consumer.accept(packet.getClass(), annotation.commandId(), annotation.cross());
		}
	}

	private void loadPacketByResourceScan(MetadataReaderFactory factory, Resource[] resources, Consumer consumer) throws IOException, ClassNotFoundException {
		final String commandId = "commandId";
		final String cross = "cross";

		for (Resource resource : resources) {
			if (!resource.isReadable()) continue;

			MetadataReader metadataReader = factory.getMetadataReader(resource);
			AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
			Map<String, Object> attr = annotationMetadata.getAnnotationAttributes(Packet.class.getName());

			if (MapUtils.isEmpty(attr)) continue;

			//noinspection unchecked
			Class<? extends AbstractPacket> clazz = (Class<? extends AbstractPacket>) Class.forName(annotationMetadata.getClassName());
			consumer.accept(clazz, (short[]) attr.get(commandId), (boolean) attr.get(cross));
		}
	}

	private void loadPacketByASM(MetadataReaderFactory factory, Resource[] resources, final Consumer consumer) throws IOException {
		final int api = Opcodes.ASM4;
		final String packetDesc = "L" + Packet.class.getName().replace('.', '/') + ";";
		final String commandIdStr = "commandId";
		final String crossStr = "cross";

		for (Resource resource : resources) {
			if (!resource.isReadable()) continue;

			if (resource.getFilename().endsWith(JavaFileObject.Kind.CLASS.extension)) {
				// 去掉后面的.class后缀
				final String className = resource.getFilename().substring(0, resource.getFilename().length() - 6);
				ClassReader classReader = new ClassReader(className);
				classReader.accept(new ClassVisitor(api) {
					@Override
					public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
						if (packetDesc.equals(desc)) {
							return new AnnotationVisitor(api) {
								short[] commandId;
								boolean cross;

								@Override
								public void visit(String name, Object value) {
									if (commandIdStr.equals(name)) {
										this.commandId = (short[]) value;
									} else if (crossStr.equals(name)) {
										this.cross = (boolean) value;
									}
									super.visit(name, value);
								}

								@Override
								public void visitEnd() {
									try {
										//noinspection unchecked
										Class<? extends AbstractPacket> clazz = (Class<? extends AbstractPacket>) Class.forName(className);
										consumer.accept(clazz, commandId, cross);
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									}
								}
							};
						}
						return null;
					}
				}, ClassReader.SKIP_CODE);
			}
		}
	}
}

interface Consumer {
	void accept(Class<? extends AbstractPacket> clazz, short[] commandIds, boolean cross);
}
