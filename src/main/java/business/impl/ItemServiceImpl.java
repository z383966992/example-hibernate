package business.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import model.Item;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import utils.ResponsesDTO;
import utils.ReturnCode;
import business.ItemService;
import business.proxy.IEntityDaoProxy;
import business.proxy.IListDaoProxy;
import business.proxy.IMappedDaoProxy;
import controller.ItemController;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

	@Resource
	private IEntityDaoProxy<Item, Integer> entityDaoProxy;

	private static Logger logger = Logger.getLogger(ItemController.class);

	@Resource
	private IListDaoProxy<Item, Integer> listDaoProxy;
	
	@Resource
	private IMappedDaoProxy mappedDaoProxy;
	
	private final static String LISTCACHE_ITEM_ALL = "listcache.item_all";
	private final static String LISTCACHE_ITEM_BY_NAME = "listcache.item_by_name";
	private final static String LISTCACHE_ITEM_BY_NAME_PRICE = "listcache.item_by_name_price";
	
	private final static String MAPPEDCACHE_ID_ORDERNUM = "mappedcache.orderNum_item";
	
	@Override
	public ResponsesDTO getItemsByName(String name) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		
		try {
			List<Object> param = new LinkedList<Object>();
			param.add(name);
			//获得id
			List<String> ids = listDaoProxy.get(LISTCACHE_ITEM_BY_NAME, param);
			
			Integer[] idArr = new Integer[ids.size()];
			for (int i = 0; i < idArr.length; i++) {
				idArr[i] = Integer.valueOf(ids.get(i));
			}
			
			List<Item> list = entityDaoProxy.getMulti(Item.class, idArr, true);
			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
			response.setAttach(list);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return response;
		}
	}
	
	/**
	 * 获取操作，不需要事务
	 * 得到所有的item
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponsesDTO getItems(String name, Integer pageSize, Integer currentPage) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		
		try {
			List<Object> param = new LinkedList<Object>();
			
			//获得id
			List<String> ids = listDaoProxy.get(LISTCACHE_ITEM_ALL, param);
			
			Integer[] idArr = new Integer[ids.size()];
			for (int i = 0; i < idArr.length; i++) {
				idArr[i] = Integer.valueOf(ids.get(i));
			}
			
			List<Item> list = entityDaoProxy.getMulti(Item.class, idArr, true);
			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
			response.setAttach(list);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return response;
		}
		
		
		
//		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
//		try {
//			
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			
//			if (!StringUtils.isEmpty(name)) {
//				paramMap.put("name", name);
//			}
//			
//			//获得总记录数量
//			int totalCount = itemMapper.totalItemsCount(paramMap);
//
//			
//			Page<Item> page = new Page<Item>(null, totalCount, pageSize, currentPage);
//			
//			paramMap.put("limit", pageSize);
//			paramMap.put("offset", page.getBeginIndex());
//			
//			List<Item> list = itemMapper.getItems(paramMap);
//
//			page.setContentList(list);
//			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
//			response.setAttach(page);
//			return response;
//		} catch (Exception e) {
//			return response;
//		}
	}

	@Override
	public ResponsesDTO insertItem(Item item) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		try {
			entityDaoProxy.save(item);
//			listDaoProxy.addIdToListCache(LISTCACHE_ITEM_ALL, item);
//			listDaoProxy.addIdToListCache(LISTCACHE_ITEM_BY_NAME, item);
			listDaoProxy.addIdToListCache(LISTCACHE_ITEM_BY_NAME_PRICE, item);
			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
			response.setAttach(null);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.fillInStackTrace());
			return response;
		}
	}
	
	
	@Override
	public ResponsesDTO deleteItem(Item item) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		try{
			entityDaoProxy.delete(Item.class, item);
//			listDaoProxy.removeIdFromListCache(LISTCACHE_ITEM_ALL, item);
			listDaoProxy.removeIdFromListCache(LISTCACHE_ITEM_BY_NAME, item);
			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
			response.setAttach(null);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return response;
		}
	}
	

	@Override
	public ResponsesDTO getItemById(Integer id) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		try {
			Item item = entityDaoProxy.get(Item.class, id);
			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
			response.setAttach(item);
			return response;
		} catch (Exception e) {
			logger.error(e.fillInStackTrace());
			return response;
		}
	}

	@Override
	public ResponsesDTO update(Item item) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		try {
			entityDaoProxy.update(item);
			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
			response.setAttach(null);
			return response;
		} catch (Exception e) {
			logger.error(e.fillInStackTrace());
			return response;
		}
	}

	@Override
	public ResponsesDTO getMulti(Integer[] ids, boolean filterNullValue) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		try {
			List<Item> result = entityDaoProxy.getMulti(Item.class, ids, filterNullValue);
			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
			response.setAttach(result);
			return response;
		} catch (Exception e) {
			logger.error(e.fillInStackTrace());
			return response;
		}
	}

	@Override
	public ResponsesDTO getFromOrderNum(String orderNum) {
		ResponsesDTO response = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		try {
			List<Object> params = new LinkedList<Object>();
			params.add(orderNum);
			String value = mappedDaoProxy.get(MAPPEDCACHE_ID_ORDERNUM, params);
			Item item = entityDaoProxy.get(Item.class, Integer.valueOf(value));
			response.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
			response.setAttach(item);
			return response;
		} catch (Exception e) {
			logger.error(e.fillInStackTrace());
			return response;
		}
	}
}
