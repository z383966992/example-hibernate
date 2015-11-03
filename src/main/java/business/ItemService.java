package business;

import model.Item;
import utils.ResponsesDTO;

public interface ItemService {
	
	/**
	 * listDaoProxy.get
	 * @param name
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	ResponsesDTO getItems(String name, Integer pageSize, Integer currentPage);
	
	/**
	 * listDaoProxy.get
	 * @param name
	 * @return
	 */
	ResponsesDTO getItemsByName(String name);

	/**
	 * entityDao
	 * listDaoProxy.addIdToListCache
	 * @param item
	 * @return
	 */
	ResponsesDTO insertItem(Item item);
	
	/**
	 * entityDao
	 * listDaoProxy.removeIdFromListCache
	 * 删除item并且在list_all中删除
	 * @param item
	 * @return
	 */
	ResponsesDTO deleteItem(Item item);
	
	/**
	 * entityDao
	 * @param id
	 * @return
	 */
	ResponsesDTO getItemById(Integer id);
	
	/**
	 * entityDao
	 * @param item
	 * @return
	 */
	ResponsesDTO update(Item item);
	
	/**
	 * entityDao
	 * @param ids
	 * @param filterNullValue
	 * @return
	 */
	ResponsesDTO getMulti(Integer []ids, boolean filterNullValue);
	
	/**
	 * 
	 * 通过orderNum获得item
	 * @param orderNum
	 * @return
	 */
	ResponsesDTO getFromOrderNum(String orderNum);
	
	
}
