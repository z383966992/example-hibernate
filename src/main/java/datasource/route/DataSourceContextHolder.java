package datasource.route;

import datasource.DataSourceType;

public class DataSourceContextHolder {
	
	private static final ThreadLocal<DataSourceType> contextHolder = 
			new ThreadLocal<DataSourceType>();
	
	public static void setDataSource(DataSourceType dataSourceType) {
		contextHolder.set(dataSourceType);
	}
	
	public static DataSourceType getDataSource() {
		return (DataSourceType) contextHolder.get();
	}
	
	public static void clearDataSource() {
		contextHolder.remove();
	}
}
