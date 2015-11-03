package datasource;

import java.util.Random;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import datasource.select.DataSourceSelectStrategy;

@Aspect
public class DatasourceController {

//	private static Random random = new Random();
	
	@Resource
	private DataSourceSelectStrategy strategy;
	
	@Pointcut("execution(* business.impl.*.*(..))")
	private void allServiceMethod(){}
	
	/**
	 * 随机选取一个数据源
	 */
	@Before("allServiceMethod()")
	public void dataSourceSelect(JoinPoint jp) {
		
		strategy.dataSourceSelect(jp);
		/*System.out.println(jp.getSignature().getName());
		int n = random.nextInt();
		if (n%2 == 0) {
			System.out.println("slave  " + n );
			DataSourceContextHolder.setCustomerType(DataSourceType.SLAVE);
		} else {
			System.out.println("master  " + n );
			DataSourceContextHolder.setCustomerType(DataSourceType.MASTER);
		}*/
	}
}