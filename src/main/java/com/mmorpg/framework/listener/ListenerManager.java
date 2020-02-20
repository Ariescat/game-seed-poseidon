package com.mmorpg.framework.listener;

import com.mmorpg.framework.listener.annotation.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.Map.Entry;

/**
 * 监听管理器
 */
public class ListenerManager {

	private final static Logger LOGGER = LoggerFactory.getLogger(ListenerManager.class);
	private static ListenerManager listenerMgr = new ListenerManager();
	private final static Map<Class<?>, List<?>> listenerMap = new HashMap<Class<?>, List<?>>();

	private ListenerManager() {
	}

	public static ListenerManager getInstance() {
		return listenerMgr;
	}

	public void init(ApplicationContext context) {
		Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Listener.class);
		Set<Class<?>> listenerSet = new HashSet<Class<?>>();
		for (Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
			Class<?>[] interfaces = entry.getValue().getClass().getInterfaces();
			for (Class<?> interf : interfaces) {
				if (interf.isAnnotationPresent(Listener.class)) {
					listenerSet.add(interf);
				}
			}
		}
		for (Class<?> listener : listenerSet) {
			Map<String, ?> beansOfType = context.getBeansOfType(listener);
			List list = listenerMap.get(listener);
			if (list == null) {
				list = new ArrayList();
				listenerMap.put(listener, list);
			}
			for (Entry<String, ?> entry : beansOfType.entrySet()) {
				list.add(entry.getValue());
			}
//			sortByIndex(list);
		}
//		info();
	}

	/**
	 * 按照annotation Index 制定的顺序排序
	 *
	 * @param list
	 */
	private static <T> void sortByIndex(List<T> list) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				Listener index1 = o1.getClass().getAnnotation(Listener.class);
				Listener index2 = o2.getClass().getAnnotation(Listener.class);
				return index1.index() > index2.index() ? -1 : 1;
			}
		});
	}

	private Listener findListener(Object o) {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		Class<?> clazz = o.getClass();
		classList.add(clazz);

		for (Class<?> cz : o.getClass().getInterfaces()) {
			classList.add(cz);
		}

		while (true) {
			if (clazz.getSuperclass() != Object.class) {
				clazz = clazz.getSuperclass();
				classList.add(clazz);
				for (Class<?> cz : clazz.getInterfaces()) {
					classList.add(cz);
				}
				continue;
			}
			break;
		}
		for (Class<?> cz : classList) {
			if (cz.isAnnotationPresent(Listener.class)) {
				return cz.getAnnotation(Listener.class);
			}
		}
		return null;
	}

	private void info() {
		log("");
		log("#######################################################");
		log("# Listener has found as following infomation:");
		log("#######################################################");
		Set<Entry<Class<?>, List<?>>> entrySet = listenerMap.entrySet();
		int index1 = 0;
		for (Entry<Class<?>, List<?>> entry : entrySet) {
			index1++;
			log("#" + index1 + "--------" + entry.getKey().getName());
			List<?> value = entry.getValue();
			int index2 = 0;
			for (Object o : value) {
				index2++;
				log("#   |" + index2 + "-----" + o.getClass().getName());
			}
		}
		log("#######################################################");
		log("");
	}

	private void log(String msg) {
		LOGGER.info(msg);
	}

	/**
	 * 获取某种类型的监听器
	 *
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getListeners(Class<T> clazz) {
		List<T> list = (List<T>) listenerMap.get(clazz);
		if (list == null) {
			list = new ArrayList<T>(0);
			listenerMap.put(clazz, list);
		}
		return list;
	}

	/**
	 * 玩家创建角色触发
	 * @param player
	 */
//	public void operateCreatePlayer(IPlayer player) {
//		Collection<IPlayerCreateListener> list = getListeners(IPlayerCreateListener.class);
//		for (IPlayerCreateListener listener : list) {
//			try {
//				listener.onCreatePlayer(player);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 玩家登陆触发
//	 * @param player
//	 */
//	public void afterPlayerOnLogin(IPlayer player) {
//		Collection<AfterLoginListener> list = getListeners(AfterLoginListener.class);
//		for (AfterLoginListener listener : list) {
//			try {
//				listener.onLogin(player);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 玩家退出触发
//	 * @param player
//	 */
//	public void afterPlayerOnlogout(IPlayer player) {
//		Collection<AfterLogoutListener> list = getListeners(AfterLogoutListener.class);
//		for (AfterLogoutListener listener : list) {
//			try {
//				listener.onLogout(player);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
