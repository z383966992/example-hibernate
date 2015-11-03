package datasource.select;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器，轮询用
 * @author zhouliangliang1
 *
 */
public class Counter {

	private static AtomicInteger slaveCounter = new AtomicInteger(0);
	private static AtomicInteger masterCounter = new AtomicInteger(0);
	
	private Counter(){
		
	}
	
	//slave counter
	public static int addSlaveCounter() {
		return slaveCounter.addAndGet(1);
	}
	
	public static void resetSlaveCounter() {
		slaveCounter.set(0);
	}
	
	public static int getSlaveCounter(){
		return slaveCounter.get();
	}
	
	//master counter
	public static int addMasterCounter() {
		return masterCounter.addAndGet(1);
	}
	
	public static void resetMasterCounter() {
		masterCounter.set(0);
	}
	
	public static int getMasterCounter(){
		return masterCounter.get();
	}
}
