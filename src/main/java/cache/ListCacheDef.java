package cache;

import java.util.Map;

public class ListCacheDef {


	private String name;

	private String cacheType;

	private String className;

	private String sql;

	private String maxListSize;

	private String key;

	private String elementId;

	private Map<String, String> conditionMap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCacheType() {
		return cacheType;
	}

	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getMaxListSize() {
		return maxListSize;
	}

	public void setMaxListSize(String maxListSize) {
		this.maxListSize = maxListSize;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public Map<String, String> getConditionMap() {
		return conditionMap;
	}

	public void setConditionMap(Map<String, String> conditionMap) {
		this.conditionMap = conditionMap;
	}


}
