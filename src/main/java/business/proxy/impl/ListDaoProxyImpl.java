package business.proxy.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import model.AbstractEntity;
import model.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import business.proxy.AbstractListDaoProxy;
import business.proxy.CacheKeyUtil;
import business.proxy.IListDaoProxy;
import cache.CacheDefManager;
import cache.ICacheManager;
import cache.ListCacheDef;
import dao.IBaseDao;

@Service
public class ListDaoProxyImpl<E extends AbstractEntity<PK>, PK extends Serializable>
		extends AbstractListDaoProxy<E, PK> implements IListDaoProxy<E, PK> {
	private static final Logger log = LoggerFactory.getLogger(ListDaoProxyImpl.class);
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Resource
	private ICacheManager cacheManager;
	
	@Resource
	private CacheDefManager cacheDefManager;
	
	@Resource
	private IBaseDao<Item, Integer> baseDao;
	
	
	@Override
	public List<String> get(String listCacheName, List<Object> params) {
		final String KEY = CacheKeyUtil.getListCacheKey(listCacheName, params);
		List<String> idList = null;	
		lock.readLock().lock();
		try {
			idList = cacheManager.lrange(KEY);
			if (idList != null && idList.size() > 0) {				
				return idList;
			}
		} finally {
			lock.readLock().unlock();
		}

		// 没有命中cache, 查询db
		ListCacheDef def = cacheDefManager
				.getListCacheDefByListName(listCacheName);
		final String sql = def.getSql();
		List<Integer> idIntList = baseDao.getList(sql, params);
		if(idList == null){
			idList = new ArrayList<String>();
		}
		
		for(Integer idInt : idIntList){
			idList.add(String.valueOf(idInt));
		}
		log.debug("retrieve from db. K[{}] size[{}]", KEY, idList.size());

		lock.writeLock().lock();
		try {
			cacheManager.lpush( KEY, idList);
		} finally {
			lock.writeLock().unlock();
		}
		return idList;
	}
	
	@Override
	public int getMaxListSize(String listCacheName) {
		// TODO Auto-generated method stub
		ListCacheDef def = cacheDefManager.getListCacheDefByListName(listCacheName);
		int maxListSize = Integer.parseInt(def.getMaxListSize());
		return maxListSize;
	}

	/**
	 * 往ListCache添加元素 Note: 本方法只负责往ListCache里添加, 若ListCache不存在, 直接返回, 不负责从DB获取
	 * @param listCacheName
	 * @param object
	 */
	@Override
	public void addIdToListCache(String listCacheName, E object) {
		log.debug("add id to listcache listname[{}] id[{}]", listCacheName, object.getId());
		try {
			ListCacheDef def = cacheDefManager.getListCacheDefByListName(listCacheName);
							   
			// 计算listCache的Key
			List<Method> keyMethodList = cacheDefManager.getKeyMethodListByListName(listCacheName);
			List<Object> keyParams = new ArrayList<Object>();
			for (int i = 0; keyMethodList != null && i < keyMethodList.size(); i++) {
				Method m = keyMethodList.get(i);
				Object v = m.invoke(object);			
				keyParams.add(v);
			}

			final String KEY = CacheKeyUtil.getListCacheKey(listCacheName, keyParams);
			if (cacheManager.lrange(KEY).size() <= 0) {
				log.debug("listcache not exist, can not added to. listname[{}] K[{}]", listCacheName, KEY);
				return;
			}

			// 获取加入listCache的条件
			Map<Method, String> conditionMap = cacheDefManager.getConditionMapByListName(listCacheName);
			if (conditionMap != null) {
				for (Map.Entry<Method, String> entry : conditionMap.entrySet()) {
					Method m = entry.getKey();
					String stringValue = entry.getValue();
					Class<?> returnType = m.getReturnType();

					// 将String类型对象转为实际需要的类型对象
					// 必须得到期望的对象, 否则无法与实际对象值相比较
					Object expectValue = getRealObject(returnType, stringValue);
					Object actureValue = m.invoke(object);
					if (actureValue != null && expectValue != null) {
						if (!actureValue.equals(expectValue)) {
							
							final Object[] logValue = new Object[] { listCacheName, expectValue, actureValue };
							log.debug("condition mismatch. listname[{}] expectvalue[{}] acturevalue[{}]", logValue);					
							return;
						}
					} else {
						final Object[] logValue = new Object[] { listCacheName, expectValue, actureValue };
						log.debug("condition mismatch, actureValue or expectValue is null. listname[{}] expectvalue[{}] actureValue[{}]",
									logValue);
						return;
					}
				}
			}

			// 获取listCache的元素
			Method elementIdMethod = cacheDefManager.getElementIdMethodByListName(listCacheName);
			Integer id = (Integer) elementIdMethod.invoke(object);

			// list最大长度
			int maxListSize = Integer.parseInt(def.getMaxListSize());

			lock.writeLock().lock();
			try {
				List<String> idList = cacheManager.lrange(KEY);
				idList.add(0, String.valueOf(id));
				log.debug("added id[{}] to listcache. listname[{}]", id, listCacheName);

				if (maxListSize != -1 && idList.size() > maxListSize) {
					idList.remove(idList.size() - 1);
					log.debug("put list to listcache. listname[{}] size[{}]", listCacheName, idList.size());
					cacheManager.lpush(KEY, idList);
				} else {
					log.debug("put list to listcache. listname[{}] size[{}]", listCacheName, idList.size());
					cacheManager.lpush(KEY, String.valueOf(id));
				}
			} finally {
				lock.writeLock().unlock();
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("addIdToListCache exception - unexpect returnType. listname[" + listCacheName + "]", e);
		} catch (Exception e) {
			throw new RuntimeException("addIdToListCache exception - ", e);
		}
	}

	/**
	 * 从ListCache删除元素 Note: 本方法只负责删除ListCache里的元素, 若ListCache不存在, 直接返回
	 */
	@Override
	public void removeIdFromListCache(String listCacheName, E object) {
		log.debug("remove id[{}] from listcache listname[{}]", object.getId(), listCacheName);
		try {
			List<Method> methodList = cacheDefManager.getKeyMethodListByListName(listCacheName);
			List<Object> keyParams = new ArrayList<Object>();
			for (int i = 0; i < methodList.size(); i++) {
				Method m = methodList.get(i);
				Object v = m.invoke(object);
				keyParams.add(v);
			}
			final String KEY = CacheKeyUtil.getListCacheKey(listCacheName, keyParams);

			if (cacheManager.lrange(KEY).size() <= 0) {
				log.debug("listcache not exist, can not remove. listName[{}] K[{}]", listCacheName, KEY);
				return;
			}

			ListCacheDef def = cacheDefManager.getListCacheDefByListName(listCacheName);
			// list最大长度
			int maxListSize = Integer.parseInt(def.getMaxListSize());
			if (maxListSize != -1 && getLength(listCacheName, keyParams) >= maxListSize) {
				// 如果实际元素大小cache最大长度的话, 删除元素只会减小cache长度,
				// 正确做法是应从数据库补一条进来
				// 这里暂时这样做
				remove(listCacheName, keyParams);
				return;
			}

			Method elementIdMethod = cacheDefManager.getElementIdMethodByListName(listCacheName);
			Integer id = (Integer) elementIdMethod.invoke(object);

			lock.writeLock().lock();
			try {
				List<String> idList = cacheManager.lrange( KEY);
				idList.remove(String.valueOf(id));
				log.debug("remove id from listcache. listname[{}] id[{}]", listCacheName, id);
				cacheManager.del(KEY);
				cacheManager.lpush(KEY, idList);
				log.debug("put list to listcache. listname[{}] size[{}]", listCacheName, idList.size());
			} finally {
				lock.writeLock().unlock();
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("removeIdFromListCache exception - unexpect returnType. listname[" + listCacheName + "]", e);
		} catch (Exception e) {
			throw new RuntimeException("removeIdFromListCache exception - ", e);
		}
	}

	@Override
	public long getLength(String listCacheName, List<Object> params) {
		final String KEY = CacheKeyUtil.getListCacheKey(listCacheName, params);
		lock.readLock().lock();
		try {
			long l = cacheManager.llen(KEY);
			return l;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void remove(String listCacheName, List<Object> params) {
		final String KEY = CacheKeyUtil.getListCacheKey(listCacheName, params);
		lock.writeLock().lock();
		try {
			cacheManager.del(KEY);
		} finally {
			lock.writeLock().unlock();
		}
	}

//	public void setBaseDao(IBaseDao baseDao) {
//		this.baseDao = baseDao;
//	}
//	
//	public void setCacheDefManager(CacheDefManager cacheDefManager) {
//		this.cacheDefManager = cacheDefManager;
//	}
//
//	public void setCacheManager(ICacheManager cacheManager) {
//		this.cacheManager = cacheManager;
//	}

}
