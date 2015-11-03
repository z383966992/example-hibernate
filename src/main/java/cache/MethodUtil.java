package cache;

import java.lang.reflect.Method;

public class MethodUtil {


	/**
	 * 获取get方法名
	 * 
	 * @param name
	 * @return
	 */
	public static String convertToMehtodName(String getterOrSetter, String name) {
		String methodName = "";
		if (name != null && !name.equals("")) {
			String first = name.substring(0, 1);
			methodName = getterOrSetter + first.toUpperCase();
			if (name.length() > 1) {
				methodName = methodName + name.substring(1);
			}
		}
		return methodName;
	}

	/**
	 * 获取Getter方法
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getGetterMethod(Class<?> clazz, String name) {
		String methodName = convertToMehtodName("get", name);
		Method method = null;
		try {
			method = clazz.getMethod(methodName);
		} catch (Exception e) {
			throw new RuntimeException("get method exception - ", e);
		}
		return method;
	}

	/**
	 * 获取Setter方法
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getSetterMethod(Class<?> clazz, String name) {
		String methodName = convertToMehtodName("set", name);
		Method method = null;
		try {
			method = clazz.getMethod(methodName, String.class);
		} catch (Exception e) {
			throw new RuntimeException("get method exception - ", e);
		}
		return method;
	}


}
