package business.proxy;

import java.io.Serializable;

import model.AbstractEntity;

public abstract class AbstractListDaoProxy<E extends AbstractEntity<PK>, PK extends Serializable> implements IListDaoProxy<E, PK> {

	/**
	 * 将value转成实际的返回类型对象
	 * 
	 * @param returnType
	 * @param value
	 * @return
	 */
	protected Object getRealObject(Class<?> returnType, String value) throws IllegalArgumentException {
		if (returnType == byte.class) {
			return Byte.parseByte(value);
		}
		if (returnType == Integer.class || returnType == int.class) {
			return Integer.parseInt(value);
		}
		if (returnType == String.class) {
			return value;
		}
		if (returnType == Long.class || returnType == long.class) {
			return Long.parseLong(value);
		}
		throw new IllegalArgumentException("unexpect returnType: " + returnType);
	}

}
