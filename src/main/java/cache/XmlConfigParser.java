package cache;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlConfigParser extends DefaultHandler{


	private static final Logger log = LoggerFactory.getLogger(XmlConfigParser.class);

	// ListCache映射
	private Map<String, ListCacheDef> listCacheDefMap = new HashMap<String, ListCacheDef>();

	// PageCache映射
	private Map<String, PageCacheDef> pageCacheDefMap = new HashMap<String, PageCacheDef>();

	// MappedCache映射
	private Map<String, MappedCacheDef> mappedCacheDefMap = new HashMap<String, MappedCacheDef>();

	private ListCacheDef listCacheDef;

	private PageCacheDef pageCacheDef;

	private MappedCacheDef mappedCacheDef;

	// 条件
	private Map<String, String> conditionMap;

	// 当前节点
	private String currentTopNodeQName;

	public XmlConfigParser() {
	}

	public Map<String, ListCacheDef> getListCacheDefMap() {
		return this.listCacheDefMap;
	}

	public Map<String, PageCacheDef> getPageCacheDefMap() {
		return this.pageCacheDefMap;
	}

	public Map<String, MappedCacheDef> getMappedCacheDefMap() {
		return this.mappedCacheDefMap;
	}

	/**
	 * 解析配置文件listcachemapping.xml
	 * 
	 * @param uri
	 *            String
	 * @param localName
	 *            String
	 * @param qName
	 *            String
	 * @param attributes
	 *            Attributes
	 * @throws SAXException
	 */
	public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {

		if (qName == null)
			return;

		if (qName.equalsIgnoreCase("listcache")) {
			currentTopNodeQName = qName;
			parseListCacheDefNode(attributes);
		}

		if (qName.equalsIgnoreCase("pagecache")) {
			currentTopNodeQName = qName;
			parsePageCacheDefNode(attributes);
		}

		if (qName.equalsIgnoreCase("mappedcache")) {
			currentTopNodeQName = qName;
			parseMappedCacheDefNode(attributes);
		}

		if (qName.equalsIgnoreCase("conditions")) {
			conditionMap = new HashMap<String, String>();
		}

		if (qName.equalsIgnoreCase("field")) {
			String fieldName = null;
			String fieldValue = null;
			for (int i = 0; i < attributes.getLength(); i++) {
				final String attrName = attributes.getQName(i);
				final String attrValue = attributes.getValue(i);
				if (attrName.equals("name")) {
					fieldName = attrValue;
				}
				if (attrName.equals("value")) {
					fieldValue = attrValue;
				}
				if (fieldName != null && fieldValue != null) {
					conditionMap.put(fieldName, fieldValue);
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName == null)
			return;

		if (qName.equalsIgnoreCase("listcache")) {
			listCacheDefMap.put(listCacheDef.getName(), listCacheDef);
			currentTopNodeQName = null;

			log.debug("parse listcache [" + listCacheDef.getName() + "] ok.");

		}
		if (qName.equalsIgnoreCase("pagecache")) {
			pageCacheDefMap.put(pageCacheDef.getName(), pageCacheDef);
			currentTopNodeQName = null;

			log.debug("parse pagecache [" + pageCacheDef.getName() + "] ok.");

		}
		if (qName.equalsIgnoreCase("mappedcache")) {
			mappedCacheDefMap.put(mappedCacheDef.getName(), mappedCacheDef);
			currentTopNodeQName = null;

			log.debug("parse mappedcache [" + mappedCacheDef.getName() + "] ok.");

		}

		if (qName.equalsIgnoreCase("conditions")) {
			if (currentTopNodeQName.equalsIgnoreCase("listcache")) {
				listCacheDef.setConditionMap(conditionMap);
			}
			conditionMap = null;
		}

	}

	private void parseListCacheDefNode(final Attributes attributes) {
		listCacheDef = new ListCacheDef();
		try {
			for (int i = 0; i < attributes.getLength(); i++) {
				final String attrName = attributes.getQName(i);
				final String attrValue = attributes.getValue(i);

				log.debug("listcache - attrName = " + attrName + ", attrValue = " + attrValue);

				Method method = MethodUtil.getSetterMethod(ListCacheDef.class, attrName);
				method.invoke(listCacheDef, attrValue);
			}
		} catch (Exception e) {
			throw new RuntimeException("parse listcache exception - ", e);
		}
	}

	private void parsePageCacheDefNode(final Attributes attributes) {
		pageCacheDef = new PageCacheDef();
		try {
			for (int i = 0; i < attributes.getLength(); i++) {
				final String attrName = attributes.getQName(i);
				final String attrValue = attributes.getValue(i);

				log.debug("pagecache - attrName = " + attrName + ", attrValue = " + attrValue);

				Method method = MethodUtil.getSetterMethod(PageCacheDef.class, attrName);
				method.invoke(pageCacheDef, attrValue);
			}
		} catch (Exception e) {
			throw new RuntimeException("parse pagecache exception - ", e);
		}
	}

	private void parseMappedCacheDefNode(final Attributes attributes) {
		mappedCacheDef = new MappedCacheDef();
		try {
			for (int i = 0; i < attributes.getLength(); i++) {
				final String attrName = attributes.getQName(i);
				final String attrValue = attributes.getValue(i);

				log.debug("mappedcache - attrName = " + attrName + ", attrValue = " + attrValue);

				Method method = MethodUtil.getSetterMethod(MappedCacheDef.class, attrName);
				method.invoke(mappedCacheDef, attrValue);
			}
		} catch (Exception e) {
			throw new RuntimeException("parse mappedcache exception - ", e);
		}
	}

}
