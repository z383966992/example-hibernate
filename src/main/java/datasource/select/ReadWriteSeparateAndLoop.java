package datasource.select;

import org.aspectj.lang.JoinPoint;

import datasource.DataSourceType;
import datasource.route.DataSourceContextHolder;

/**
 * 读写分离
 * 写操作master
 * 读操作slave
 * 多个master与slave轮询
 * @author zhouliangliang1
 *
 */
public class ReadWriteSeparateAndLoop extends DataSourceSelectStrategy{
	
	@Override
	public void dataSourceSelect(JoinPoint jp) {
		
		int flag = readWriteJudge(jp);
		
		//读取操作
		if (flag == 0) {
			int size = slaveList.size();
			int index = Counter.getSlaveCounter() % size;
System.out.println();			
System.out.println("read");			
System.out.println("size : " + size);			
System.out.println("index : " + index);
System.out.println("Counter.getSlaveCounter() : " + Counter.getSlaveCounter());
System.out.println();			
			Counter.addSlaveCounter();
			DataSourceType dst = slaveList.get(index);
			System.out.println(dst.getName());
			DataSourceContextHolder.setDataSource(dst);
			if(Counter.getSlaveCounter() == slaveList.size()){
				Counter.resetSlaveCounter();;	
			}
		} else {
		//写入操作
System.out.println("write");			
			int size = masterList.size();
			int index = Counter.getMasterCounter() % size;

System.out.println();			
System.out.println("read");			
System.out.println("size : " + size);			
System.out.println("index : " + index);
System.out.println("Counter.getSlaveCounter() : " + Counter.getSlaveCounter());
System.out.println();

			
			Counter.addMasterCounter();
			DataSourceType dst = masterList.get(index);
			System.out.println(dst.getName());
			DataSourceContextHolder.setDataSource(dst);
			if(Counter.getMasterCounter() == masterList.size()){
				Counter.resetMasterCounter();;	
			}
		}
	}
}
