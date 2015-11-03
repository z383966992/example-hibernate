package dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import model.AbstractEntity;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.IBaseDao;

public class BaseDaoImpl<E extends AbstractEntity<PK>, PK extends Serializable>
		extends HibernateDaoSupport implements IBaseDao<E, PK> {
	private static final Logger log = LoggerFactory
			.getLogger(BaseDaoImpl.class);

	public E get(final Class<? extends E> clazz, final PK id) {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Object entity = session.get(clazz, id);
				return entity;
			}
		};
		Object obj = getHibernateTemplate().execute(callback);
		log.debug("get from db. class[{}] id[{}]", clazz, id);
		return (E) obj;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public PK save(final E e) {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Serializable ret = session.save(e);
				return ret;
			}
		};
		PK id = (PK) getHibernateTemplate().execute(callback);
		log.debug("save to db. class[{}] id[{}]", e.getClass(), id);
		return id;
	}

	public void update(final E e) {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session) {
				session.update(e);
				return null;
			}
		};
		getHibernateTemplate().execute(callback);
		log.debug("update to db. class[{}] id[{}]", e.getClass(), e.getId());
	}

	public void delete(final E e) {
		// TODO Auto-generated method stub
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session) {
				session.delete(e);
				return null;
			}
		};
		getHibernateTemplate().execute(callback);
		log.debug("delete from db. class[{}] id[{}]", e.getClass(), e.getId());
	}

	public void saveOrUpdate(final E e) {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session) {
				session.saveOrUpdate(e);
				return null;
			}
		};
		getHibernateTemplate().execute(callback);
		log.debug("saveOrUpdate to db. class[{}] id[{}]", e.getClass(),
				e.getId());
	}

	public int batchUpdate(final String sql, final List<Object> params) {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery(sql);
				int k = 0;
				for (int i = 0; params != null && i < params.size(); i++) {
					query.setParameter(k++, params.get(i));
				}
				return query.executeUpdate();
			}
		};
		Object obj = getHibernateTemplate().execute(callback);
		log.debug("batchUpdate to db. sql[{}] updated[{}]", sql, obj);
		return (Integer) obj;
	}

	public List<PK> getList(final String sql, final List<Object> params) {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery(sql);
				int k = 0;
				for (int i = 0; params != null && i < params.size(); i++) {
					query.setParameter(k++, params.get(i));
				}
				return query.list();
			}
		};

		// 这个list要作为listCache, 经常会add/remove, 所以包装成LinkedList
		List<PK> list = new LinkedList<PK>((List<PK>) getHibernateTemplate()
				.execute(callback));
		log.debug("getList from db. sql[{}] List.len[{}]", sql, list.size());
		return list;
	}
	
	
	public List findByHql(final String hql) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);				
				List list = query.list();
				return list;
			}
		});
	}
	
	public List queryBySql(final String sql, final List<Object> params) {
		final long tb = System.currentTimeMillis();

		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery(sql);
				int k = 0;
				for (int i = 0; params != null && i < params.size(); i++) {
					query.setParameter(k++, params.get(i));
				}
				return query.list();
			}
		};

		List list = (List) getHibernateTemplate().execute(callback);

		final long te = System.currentTimeMillis();
		log.info("Time[get] Time:" + (te - tb));

		return list;
	}
	
	@Override
	public Session getHibernateSession() {
		return this.getSession();
	}
}
