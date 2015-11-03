package business.proxy.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import model.AbstractEntity;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import utils.SerializableUtil;
import business.proxy.AbstractEntityDaoProxy;
import business.proxy.CacheKeyUtil;
import business.proxy.ICacheable;
import business.proxy.IEntityDaoProxy;
import business.proxy.IPersistable;
import cache.ICacheManager;
import dao.IBaseDao;


/**
 * EntityDaoProxyImpl根据Entity是否继承了ICacheable或者IPersistable
 * 来决定把Entity存储到数据库或(和)缓存中
 * @author zhouliangliang1
 *
 * @param <E>
 * @param <PK>
 */
@Service
public class EntityDaoProxyImpl<E extends AbstractEntity<PK>, PK extends Serializable>
		extends AbstractEntityDaoProxy<E> implements IEntityDaoProxy<E, PK> {

	private static final Logger log = LoggerFactory
			.getLogger(EntityDaoProxyImpl.class);

	@Resource
	private IBaseDao<E, PK> baseDao;

	@Resource
	private ICacheManager cacheManager;

	public E get(Class<E> clazz, PK id){

		final String KEY = CacheKeyUtil.getEntityCacheKey(clazz, id);
		E e = null;

		if (isCacheable(clazz)) {

			log.debug("get from cache KEY[" + KEY + "]");

			e = (E) (SerializableUtil.deSerialize(cacheManager.get(SerializableUtil.serialize(KEY))));
			if (e != null) {
				log.debug("return object from cache. KEY[" + KEY + "]");
				return e;
			}
		}

		if (isPersistable(clazz)) {
			e = baseDao.get(clazz, id);

			log.debug("retrieve from db. KEY[" + KEY + "] "
					+ (e != null ? "object" : "null"));

			if (e != null && isCacheable(clazz)) {

				log.debug("put object to cache in get method. KEY[" + KEY
								+ "]");

				cacheManager.set(SerializableUtil.serialize(KEY),
						SerializableUtil.serialize(e));
			}
		}

		return e;
	}


	public List<E> getMulti(Class<E> clazz, PK[] ids, boolean filterNullValue){
		List<E> list = new ArrayList<E>();
		for(PK pk : ids){
			E e = get(clazz, pk);
			if (filterNullValue == true) {
				if (e != null) {
					list.add(e);
				}
			} else {
				list.add(get(clazz, pk));
			}
		}

		return list;
	}
	
	
	/**
	 * 首先保存到数据库中
	 * 之后再以 全限定名|id 为key保存到redis中
	 */
	public void save(E e){

		if (e instanceof IPersistable) {
			PK id = baseDao.save(e);
			e.setId(id);
			log.debug("save object to db. class[" + e.getClass() + "] newid["
					+ id + "]");
		}

		if (e instanceof ICacheable) {
			final String KEY = CacheKeyUtil.getEntityCacheKey(e.getClass(), e
					.getId());

			log.debug("put object to cache in save method. KEY[" + KEY + "]");
			cacheManager.set(SerializableUtil.serialize(KEY), SerializableUtil
					.serialize(e));
		}
	}
	
	/**
	 * 首先保存到数据库中
	 * 之后再以 全限定名|id 为key保存到redis中
	 * 与上一个方法不同的是，在redis中为key设置了过期时间
	 */
	public void save(E e, int seconds) {

		if (e instanceof IPersistable) {
			PK id = baseDao.save(e);
			e.setId(id);
			log.debug("save object to db. class[" + e.getClass() + "] newid["
					+ id + "]");
		}

		if (e instanceof ICacheable) {
			final String KEY = CacheKeyUtil.getEntityCacheKey(e.getClass(), e
					.getId());

			log.debug("put object to cache in save method. KEY[" + KEY + "]");

			cacheManager.set(SerializableUtil.serialize(KEY), SerializableUtil
					.serialize(e));
			cacheManager.expire(SerializableUtil.serialize(KEY), seconds);
		}
	}

	
	/**
	 * 更新一个对象
	 */
	public void update(E e) {

		if (e instanceof IPersistable) {

			log.debug("update db. class[" + e.getClass() + "] id[" + e.getId()
					+ "]");

			baseDao.update(e);
		}

		if (e instanceof ICacheable) {
			final String KEY = CacheKeyUtil.getEntityCacheKey(e.getClass(), e
					.getId());

			log.debug("put object to cache in update method. KEY[" + KEY + "]");

			cacheManager.set(SerializableUtil.serialize(KEY), SerializableUtil
					.serialize(e));
		}
	}

	/**
	 * 逻辑删除
	 * 更新DB状态, 但不从DB中删除, 从EntityCache中删除, 同时从ListCache中删除
	 */
	public void fakeDelete(E e){

		if (e instanceof IPersistable) {

			log.debug("fakedelete from db. class[" + e.getClass() + "] id["
					+ e.getId() + "]");

			baseDao.update(e);
		}

		if (e instanceof ICacheable) {
			final String KEY = CacheKeyUtil.getEntityCacheKey(e.getClass(), e
					.getId());

			log.debug("remove from cache in fakeDelete method. KEY[" + KEY
					+ "]");

			cacheManager.del(SerializableUtil.serialize(KEY));
		}
	}

	/**
	 * 物理删除
	 */
	public void delete(Class<E> clazz, E e){

		if (e == null) {
			return;
		}
		if (e instanceof IPersistable) {

			log.debug("delete from db. class[" + e.getClass() + "] id["
					+ e.getId() + "]");

			baseDao.delete(e);
		}

		if (e instanceof ICacheable) {
			final String KEY = CacheKeyUtil.getEntityCacheKey(clazz, e.getId());

			log.debug("remove from cache in delete method. KEY[" + KEY + "]");

			cacheManager.del(SerializableUtil.serialize(KEY));
		}
	}

	/**
	 * 物理删除
	 * @param clazz
	 * @param id
	 * @throws Exception
	 */
	public void delete(Class<E> clazz, PK id) throws Exception {

		E e = get(clazz, id);
		this.delete(clazz, e);
	}

	/**
	 * 批量更新
	 */
	public int batchUpdate(String sql, List<Object> params) {

		return baseDao.batchUpdate(sql, params);
	}
	
	public Session getHibernateSession(){
		return baseDao.getHibernateSession();
	}
}
