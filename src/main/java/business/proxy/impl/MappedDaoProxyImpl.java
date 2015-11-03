package business.proxy.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import model.AbstractEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import business.proxy.CacheKeyUtil;
import business.proxy.IMappedDaoProxy;
import cache.CacheDefManager;
import cache.ICacheManager;
import cache.MappedCacheDef;
import dao.IBaseDao;

@Service
public class MappedDaoProxyImpl <E extends AbstractEntity<Integer>, Integer extends Serializable> implements IMappedDaoProxy {

	private static final Logger log = LoggerFactory.getLogger(MappedDaoProxyImpl.class);

//	private static final String REGION = "MAPPED";

	@Resource
	private IBaseDao<E, Integer> baseDao;

	@Resource
	private CacheDefManager cacheDefManager;

	@Resource
	private ICacheManager cacheManager;

	@Override
	public String get(String mappedName, List<Object> params) {
		final String KEY = CacheKeyUtil.getMappedCacheKey(mappedName, params);
		String id = cacheManager.get(KEY);
		if (id != null) {
			return id;
		}

		MappedCacheDef def = cacheDefManager.getMappedCacheDefByListName(mappedName);
		final String sql = def.getSql();
		List<Integer> idList = baseDao.getList(sql, params);
		log.debug("retrieve from db. K[{}] size[{}]", KEY, idList.size());

		if (idList != null && idList.size() > 0) {
			id = String.valueOf(idList.get(0));
		}
		if (id != null) {
			cacheManager.set(KEY, id);
		}
		return id;
	}

	@Override
	public void remove(String mappedName, List<Object> params) {
		final String KEY = CacheKeyUtil.getMappedCacheKey(mappedName, params);
		cacheManager.del(KEY);
	}
//	public void setBaseDao(IBaseDao<E, Integer> baseDao) {
//		this.baseDao = baseDao;
//	}
//
//	public void setCacheManager(ICacheManager cacheManager) {
//		this.cacheManager = cacheManager;
//	}
//
//	public void setCacheDefManager(CacheDefManager cacheDefManager) {
//		this.cacheDefManager = cacheDefManager;
//	}
}
