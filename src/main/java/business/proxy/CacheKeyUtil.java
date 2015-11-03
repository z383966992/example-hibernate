package business.proxy;

import java.util.List;

public class CacheKeyUtil {


	/**
	 * Entity Cache Key
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static final String getEntityCacheKey(Class<?> clazz, Object id) {
		return clazz.getCanonicalName() + "|" + id;
	}

	/**
	 * Entity Cache Keys
	 * 
	 * @param clazz
	 * @param ids
	 * @return
	 */
	public static final String[] getEntityCacheKey(Class<?> clazz, Object[] ids) {
		String[] entityCacheKeys = new String[ids.length];
		for (int i = 0; ids != null && i < ids.length; i++) {
			entityCacheKeys[i] = clazz.getCanonicalName() + "|" + ids[i];
		}
		return entityCacheKeys;
	}

	/**
	 * List Cache Key
	 * 
	 * @param listName
	 * @param params
	 * @return
	 */
	public static final String getListCacheKey(String listName, List<Object> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(listName);
		for (int i = 0; i < params.size(); i++) {
			sb.append("|" + params.get(i));
		}
		return sb.toString();
	}

	/**
	 * Page Cache Key
	 * 
	 * @param listName
	 * @param params
	 * @param pageNo
	 * @return
	 */
	public static final String getPageCacheKey(String listName, List<Object> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(listName);
		for (int i = 0; i < params.size(); i++) {
			sb.append("|" + params.get(i));
		}
		return sb.toString();
	}

	/**
	 * Mapping Cache Key
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static final String getMappedCacheKey(String mappedName, List<Object> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(mappedName);
		for (int i = 0; i < params.size(); i++) {
			sb.append("|" + params.get(i));
		}
		return sb.toString();
	}


}
