package dao;

import java.io.Serializable;
import java.util.List;

import model.AbstractEntity;

import org.hibernate.Session;

public interface IBaseDao <E extends AbstractEntity<PK>, PK extends Serializable>{
	
	E get(Class<? extends E> clazz, PK id);

	PK save(E e);

	void update(E e);

	int batchUpdate(String sql, List<Object> params);

	void saveOrUpdate(E e);

	void delete(E e);

	List<PK> getList(String sql, List<Object> params);
	
	List findByHql(final String hql);
	
	List queryBySql(final String sql, final List<Object> params);
	
	public Session getHibernateSession() ;
}
