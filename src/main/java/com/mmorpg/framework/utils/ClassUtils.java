package com.mmorpg.framework.utils;

import com.koloboke.function.Consumer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Ariescat
 * @version 2020/3/2 12:56
 */
public class ClassUtils {

	public static void main(String[] args) throws IOException {
		scanPackage("junit", true, new Consumer<String>() {
			@Override
			public void accept(String s) {
				System.err.println(s);
			}
		});
	}

	/**
	 * @param packageName 包名
	 * @param recursive   是否递归（循环迭代）
	 * @param consumer    类名消费者
	 */
	public static void scanPackage(String packageName, boolean recursive, Consumer<String> consumer) throws IOException {
		String packageDirPath = packageName.replace(".", "/");
		//通过当前线程得到类加载器从而得到URL的枚举
		Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(packageDirPath);
		while (urlEnumeration.hasMoreElements()) {
			/*
			 * 得到的结果大概是：
			 * 	file:/C:/XXX/game-framework-poseidon/target/classes/com/mmorpg
			 * 	jar:file:/C:/XXX/.m2/repository/junit/junit/4.13/junit-4.13.jar!/junit
			 */
			URL url = urlEnumeration.nextElement();
			String protocol = url.getProtocol(); // 大概是class文件
			if ("file".equalsIgnoreCase(protocol)) {
				// 获取包的物理路径
				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				scanFile(packageName, recursive, filePath, consumer);
			} else if (recursive && "jar".equalsIgnoreCase(protocol)) {
				// 不递归则不扫描Jar文件
				scanJar(packageDirPath, url, consumer);
			}
		}
	}

	private static void scanFile(String packageName, final boolean recursive, String packageDirPath, Consumer<String> consumer) {
		File dir = new File(packageDirPath);
		// 如果不存在 或者 也不是目录就直接结束
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		final File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (file.isDirectory() && recursive) || (file.getName().endsWith(".class"));
			}
		});
		if (files == null || files.length == 0) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				scanFile(packageName + '.' + file.getName(), recursive, file.getAbsolutePath(), consumer);
			} else {
				// 去掉后面的.class后缀，并补全类全限定名
				String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
				consumer.accept(className);
			}
		}
	}

	private static void scanJar(String packageDirPath, URL url, Consumer<String> consumer) throws IOException {
		//转换为JarURLConnection
		JarURLConnection connection = (JarURLConnection) url.openConnection();
		if (connection != null) {
			JarFile jarFile = connection.getJarFile();
			if (jarFile != null) {
				//得到该jar文件下面的类实体
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					/*
					 * entry的结果大概是这样：
					 * org/
					 * org/junit/
					 * org/junit/rules/
					 * org/junit/runners/
					 */
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					if (name.charAt(0) == '/') {
						name = name.substring(1);
					}

					// 如果已'/'结尾，是一个包
					int idx = name.lastIndexOf('/');
					if (idx != -1 && name.startsWith(packageDirPath)) {
						String packageName = name.substring(0, idx).replace('/', '.');
						if (name.contains(".class")) {
							// 去掉后面的.class后缀，并补全类全限定名
							String className = packageName + '.' + name.substring(packageName.length() + 1, name.length() - 6);
							consumer.accept(className);
						}
					}
				}
			}
		}
	}

}
