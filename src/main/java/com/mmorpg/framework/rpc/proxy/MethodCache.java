package com.mmorpg.framework.rpc.proxy;

import com.mmorpg.framework.rpc.anno.RpcMethod;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.Type;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author Ariescat
 * @version 2020/2/25 19:46
 */
public class MethodCache {

	private final static Logger log = LoggerFactory.getLogger(MethodCache.class);

	private static ConcurrentHashMapV8<MethodInfo, Method> methodInfo2method = new ConcurrentHashMapV8<>();

	private static ConcurrentHashMapV8<Integer, Method> uid2method = new ConcurrentHashMapV8<>();
	static Map<Method, Integer> method2uid = new ConcurrentHashMapV8<>();

	private static final class MethodInfo {
		private final String className;
		private final String methodName;
		private final String methodDesc;

		MethodInfo(String className, String methodName, String methodDesc) {
			this.className = className;
			this.methodName = methodName;
			this.methodDesc = methodDesc;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			MethodInfo that = (MethodInfo) o;
			return Objects.equals(className, that.className)
				&& Objects.equals(methodName, that.methodName)
				&& Objects.equals(methodDesc, that.methodDesc);
		}

		@Override
		public int hashCode() {
			return Objects.hash(className, methodName, methodDesc);
		}
	}


	static Method getOrFindMethod(int methodUid, final String clazzName, final String methodName, final String methodDesc) {
		if (methodUid != 0) {
			return uid2method.get(methodUid);
		} else {
			return getMethodByDesc(clazzName, methodName, methodDesc);
		}
	}

	private static Method getMethodByDesc(final String className, final String methodName, final String methodDesc) {
		MethodInfo methodInfo = new MethodInfo(className, methodName, methodDesc);
		return methodInfo2method.computeIfAbsent(methodInfo, new ConcurrentHashMapV8.Fun<MethodInfo, Method>() {
			@Override
			public Method apply(MethodInfo methodInfo) {
				try {
					Class<?> clazz = Class.forName(className);
					org.springframework.asm.commons.Method method = new org.springframework.asm.commons.Method(methodName, methodDesc);
					Type[] argumentTypes = method.getArgumentTypes();
					String name = method.getName();
					return clazz.getDeclaredMethod(name, MethodCache.getClass(argumentTypes));
				} catch (Exception e) {
					log.error("", e);
				}
				return null;
			}
		});

	}

	private static Class[] getClass(Type[] types) {
		Class[] classes = new Class[types.length];
		for (int i = 0; i < types.length; i++) {
			classes[i] = getClass(types[i]);
		}
		return classes;
	}

	private static Class getClass(Type type) {
		try {
			if (type.equals(Type.INT_TYPE)) {
				return int.class;
			} else if (type.equals(Type.VOID_TYPE)) {
				return void.class;
			} else if (type.equals(Type.BOOLEAN_TYPE)) {
				return boolean.class;
			} else if (type.equals(Type.BYTE_TYPE)) {
				return byte.class;
			} else if (type.equals(Type.CHAR_TYPE)) {
				return char.class;
			} else if (type.equals(Type.SHORT_TYPE)) {
				return short.class;
			} else if (type.equals(Type.DOUBLE_TYPE)) {
				return double.class;
			} else if (type.equals(Type.FLOAT_TYPE)) {
				return float.class;
			} else if (type.equals(Type.LONG_TYPE)) {
				return long.class;
			} else {
				String className = type.getInternalName().replace('/', '.');
				return Class.forName(className);
			}
		} catch (ClassNotFoundException e) {
			log.error("", e);
		}
		return null;
	}

	static void cacheMethods(final Class<?> interfaceClass) {
		if (!interfaceClass.isInterface()) {
			return;
		}
		for (final Method method : interfaceClass.getDeclaredMethods()) {
			RpcMethod annotation = method.getAnnotation(RpcMethod.class);
			if (annotation != null) {
				final int id = annotation.value();
				if (id == 0) {
					log.warn("RpcMethod 唯一ID为0 {} {}", interfaceClass, method);
				} else {
					uid2method.compute(id, new ConcurrentHashMapV8.BiFun<Integer, Method, Method>() {
						@Override
						public Method apply(Integer integer, Method old) {
							if (old == null || old.equals(method)) {
								return method;
							}
							String message = "RpcMethod 唯一ID重复[" + id + "] old:[" + old + "],new:[" + method + "]";
							throw new RuntimeException(message);
						}
					});
					method2uid.put(method, id);
				}
			}
		}
	}
}
