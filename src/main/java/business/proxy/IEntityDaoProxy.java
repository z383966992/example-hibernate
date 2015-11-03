package business.proxy;

import java.util.List;

import org.hibernate.Session;

public interface IEntityDaoProxy<E, PK> {

	/**
	 * 从Cache取对象, 无结果再从DB取
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	E get(Class<E> clazz, PK id) ;

	/**
	 * 批量获取实体 从Cache取对象, 无结果再从DB取
	 * 
	 * @param clazz
	 * @param ids
	 * @return
	 */
	List<E> getMulti(Class<E> clazz, PK[] ids, boolean filterNullValue);

	void save(E e);

	public void save(E e, int seconds);

	void update(E e);

	/**
	 * 批量更新
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	int batchUpdate(String sql, List<Object> params);

	/**
	 * 批量查询
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	// List<E> batchQuery(String sql, final List<Object> param) ;

	/**
	 * 批量查询
	 * 
	 * @param instance
	 * @return
	 */
	// List<E> queryByExample(E instance) ;

	void fakeDelete(E e);

	void delete(Class<E> clazz, E e);

	// void delete(Class<E> clazz, PK id);
	//
	// void delete(Class<E> clazz, E e);
	
	public Session getHibernateSession();

}
