package business.proxy;

import java.util.List;

public interface IListDaoProxy<E, PK>{
	
	/**
	 * 从Cache或DB里获取List
	 * params 会与listCacheName一起组合成key，从cache里获取数据
	 * 如果数据没有获取到，params会形成sql语句，从db里获取
	 * @param listCacheName
	 * @param params
	 * @return
	 */
	List<String> get(String listCacheName, List<Object> params);	
	
	/**
	 * 这个方法用来获得list cache size 限制
	 * @param listCacheName
	 * @return
	 */
	int getMaxListSize(String listCacheName);

	/**
	 * 往ListCache添加元素 Note: 本方法只负责往ListCache里添加, 若ListCache不存在, 直接返回, 不负责从DB获取
	 * @param listCacheName
	 * @param object
	 */
	void addIdToListCache(String listCacheName, E object);

	/**
	 * 从ListCache删除元素 Note: 本方法只负责删除ListCache里的元素, 若ListCache不存在, 直接返回
	 * @param listCacheName
	 * @param object
	 */
	void removeIdFromListCache(String listCacheName, E object);
	
	/**
	 * 获得listCache缓存的value数量
	 * params 会与listCacheName一起组合成key
	 * @param listCacheName
	 * @param params
	 * @return
	 */
	long getLength(String listCacheName, List<Object> params);
	
	/**
	 * 删除listCache缓存
	 * params 会与listCacheName一起组合成key
	 * @param listCacheName
	 * @param params
	 */
	void remove(String listCacheName, List<Object> params);
}
