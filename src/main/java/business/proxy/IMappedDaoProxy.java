package business.proxy;

import java.util.List;

public interface IMappedDaoProxy {
	String get(String mappedName, List<Object> params);

	void remove(String mappedName, List<Object> params);
}
