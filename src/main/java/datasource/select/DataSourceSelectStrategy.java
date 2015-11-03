package datasource.select;

import java.util.LinkedList;
import java.util.List;

import org.aspectj.lang.JoinPoint;

import datasource.DataSourceType;

public abstract class DataSourceSelectStrategy {

	// 存放master
	protected static List<DataSourceType> masterList = new LinkedList<DataSourceType>();
	// 存放slave
	protected static List<DataSourceType> slaveList = new LinkedList<DataSourceType>();

	protected void init() {
		System.out.println("static");
		// 读取DataSourceType中内容
		for (DataSourceType dst : DataSourceType.values()) {
			if (dst.getName().toLowerCase().startsWith("master")) {
				masterList.add(dst);
			} else {
				slaveList.add(dst);
			}
		}
		System.out.println("masterList.size()" + masterList.size());
		System.out.println("slaveList.size()" + slaveList.size());
	}

	// static{
	// System.out.println("static");
	// //读取DataSourceType中内容
	// for(DataSourceType dst : DataSourceType.values()) {
	// if (dst.getName().toLowerCase().startsWith("master")) {
	// masterList.add(dst);
	// } else {
	// slaveList.add(dst);
	// }
	// }
	// System.out.println(masterList.size());
	// System.out.println(slaveList.size());
	// }

	/**
	 * 根据函数名称判断是读取还是写入操作
	 * 
	 * @return 0 读取 1 写入
	 */
	protected int readWriteJudge(JoinPoint jp) {
		String name = jp.getSignature().getName();
		String lowerName = name.toLowerCase();
		if (lowerName.startsWith("select") || lowerName.startsWith("get")
				|| lowerName.startsWith("fetch")) {
			return 0;
		} else {
			return 1;
		}
	}

	public abstract void dataSourceSelect(JoinPoint jp);

}
