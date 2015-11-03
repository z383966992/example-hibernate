package business.proxy;

import java.util.HashMap;
import java.util.Map;

import model.AbstractEntity;

public class AbstractEntityDaoProxy<E extends AbstractEntity> {

	// 缓存, Class是否实现ICacheable
	private Map<Class<?>, Boolean> cacheableMap = new HashMap<Class<?>, Boolean>();

	// 缓存, Class是否实现IPersistable
	private Map<Class<?>, Boolean> persistableMap = new HashMap<Class<?>, Boolean>();

	/**
	 * 判断clazz是否实现了ICacheable
	 * 
	 * @param clazz
	 * @return
	 */
	protected boolean isCacheable(Class<E> clazz) {
		Boolean ret = cacheableMap.get(clazz);
		if (ret != null) {
			return ret;
		}

		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces.length == 0) {
			return false;
		}

		for (int i = 0; i < interfaces.length; i++) {
			Class<?> interface1 = interfaces[i];
			if (interface1.getCanonicalName().equals("business.proxy.ICacheable")) {
				cacheableMap.put(clazz, Boolean.TRUE);
				return true;
			}
		}
		cacheableMap.put(clazz, Boolean.FALSE);
		return false;
	}

	/**
	 * 判断clazz是否实现了IPersistable
	 * 
	 * @param clazz
	 * @return
	 */
	protected boolean isPersistable(Class<E> clazz) {
		Boolean ret = persistableMap.get(clazz);
		if (ret != null) {
			return ret;
		}

		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces.length == 0) {
			return false;
		}

		for (int i = 0; i < interfaces.length; i++) {
			Class<?> interface1 = interfaces[i];
			if (interface1.getCanonicalName().equals("business.proxy.IPersistable")) {
				persistableMap.put(clazz, Boolean.TRUE);
				return true;
			}
		}
		persistableMap.put(clazz, Boolean.FALSE);
		return false;
	}
}
