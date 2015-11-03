package cache;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheDefManager {


	private static final Logger log = LoggerFactory.getLogger(CacheDefManager.class);

	private String cacheDefsFile;

	/**
	 * listName -> ListCacheDef
	 */
	private Map<String, ListCacheDef> listCacheDefMap;

	/**
	 * className -> ListCacheDefs
	 */
	private Map<String, List<ListCacheDef>> classNameToListCacheDefMap = new HashMap<String, List<ListCacheDef>>();

	/**
	 * listName -> key method list
	 */
	private Map<String, List<Method>> keyMethodsMap = new HashMap<String, List<Method>>();

	/**
	 * listName -> elementId method
	 */
	private Map<String, Method> elementIdMethodMap = new HashMap<String, Method>();

	/**
	 * listName -> condition Method Map
	 * 
	 */
	private Map<String, Map<Method, String>> conditionMap = new HashMap<String, Map<Method, String>>();

	/**
	 * pageName -> PageCacheDef
	 */
	private Map<String, PageCacheDef> pageCacheDefMap;

	/**
	 * mappedName -> MappedCacheDef
	 */
	private Map<String, MappedCacheDef> mappedCacheDefMap;

	private CacheDefManager() {
	}

	@SuppressWarnings("unused")
	private void init() throws Exception {
		this.configure();
		this.initClassListCacheDef();
	}

	public ListCacheDef getListCacheDefByListName(String listName) {
		return listCacheDefMap.get(listName);
	}

	public List<ListCacheDef> getListCacheDefByClass(String className) {
		return classNameToListCacheDefMap.get(className);
	}

	public List<Method> getKeyMethodListByListName(String listName) {
		return keyMethodsMap.get(listName);
	}

	public Method getElementIdMethodByListName(String listName) {
		return elementIdMethodMap.get(listName);
	}

	public PageCacheDef getPageCacheDefByListName(String listName) {
		return pageCacheDefMap.get(listName);
	}

	public MappedCacheDef getMappedCacheDefByListName(String listName) {
		return mappedCacheDefMap.get(listName);
	}

	public Map<Method, String> getConditionMapByListName(String listName) {
		return conditionMap.get(listName);
	}

	/**
	 * 将ListCacheDef分解成若干映射关系
	 * 
	 * @throws Exception
	 */
	private void initClassListCacheDef() throws Exception {
		Iterator<Map.Entry<String, ListCacheDef>> itr = listCacheDefMap.entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry<String, ListCacheDef> entry = itr.next();

			ListCacheDef listCacheDef = entry.getValue();

			String name = listCacheDef.getName();
			String className = listCacheDef.getClassName();

			Class<?> clazz = Class.forName(className);

			// 类名称 -> listCacheDef列表, 用于保存/删除对象时对各种listCacheDef的增减
			List<ListCacheDef> list = classNameToListCacheDefMap.get(className);
			if (list == null) {
				list = new ArrayList<ListCacheDef>();
			}
			list.add(listCacheDef);
			classNameToListCacheDefMap.put(className, list);

			// listName -> 获取listCache元素的反射方法
			String elementId = listCacheDef.getElementId();
			String elementIdMethodName = MethodUtil.convertToMehtodName("get", elementId);
			Method elementIdMethod = clazz.getMethod(elementIdMethodName);
			elementIdMethodMap.put(name, elementIdMethod);

			// listName -> 用于计算listCache的Key的反射方法集合
			String key = listCacheDef.getKey();
			if (!key.equals("")) {
				String[] keys = key.split("\\+");
				List<Method> methodList = new ArrayList<Method>();
				for (int i = 0; i < keys.length; i++) {
					String methodName = MethodUtil.convertToMehtodName("get", keys[i]);
					methodList.add(clazz.getMethod(methodName));
				}
				keyMethodsMap.put(name, methodList);
			} else {
				keyMethodsMap.put(name, new ArrayList<Method>());
			}

			// listName -> 判别元素进入/移出listCache的条件, 条件是反射方法和期望值
			Map<String, String> conditionStringMap = listCacheDef.getConditionMap();
			if (conditionStringMap != null) {
				Map<Method, String> conditionMethodMap = new HashMap<Method, String>();
				Iterator<Map.Entry<String, String>> fieldMapItr = conditionStringMap.entrySet().iterator();
				while (fieldMapItr.hasNext()) {
					Map.Entry<String, String> fieldMapEntry = fieldMapItr.next();
					String field = fieldMapEntry.getKey();
					String value = fieldMapEntry.getValue();
					String methodName = MethodUtil.convertToMehtodName("get", field);
					Method m = clazz.getMethod(methodName);
					conditionMethodMap.put(m, value);
				}
				conditionMap.put(name, conditionMethodMap);
			}
		}
	}

	/**
	 * Configures from an XML file in the classpath.
	 * 
	 * @throws Exception
	 */
	public void configure() throws Exception {
		log.info("starting config cache defines ...");
		final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		final XmlConfigParser handler = new XmlConfigParser();
		URL url = getClass().getResource(cacheDefsFile);
		if (url != null) {

			log.debug("cache defines file [" + cacheDefsFile + "] found in the classpath: " + url);

		}
		parser.parse(url.toExternalForm(), handler);
		this.listCacheDefMap = handler.getListCacheDefMap();
		this.pageCacheDefMap = handler.getPageCacheDefMap();
		this.mappedCacheDefMap = handler.getMappedCacheDefMap();

		log.info("config cache defines finished.");
	}

	public void setCacheDefsFile(String cacheDefsFile) {
		this.cacheDefsFile = cacheDefsFile;
	}
}
