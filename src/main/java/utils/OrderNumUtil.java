package utils;

import org.apache.commons.lang.time.DateFormatUtils;

public class OrderNumUtil {
	
	/**
	 * 创建订单号。规则为取毫秒级时间在加三位随机数
	 * 
	 * @return
	 */
	public static String create() {
		long now = System.currentTimeMillis();
		int randomNum = (int) (Math.random() * 100);
		String r = randomNum < 100 ? "0" + randomNum : "" + randomNum;
		r = randomNum < 10 ? "0" + r : "" + r;
		String orderNum = DateFormatUtils.format(now, "yyyyMMddHHmmssSSS") + r;
		return orderNum;
	}
	
	public static void main(String[] args) {
		System.out.println(new OrderNumUtil().create());
	}

}
