package business.test;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import model.Item;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utils.OrderNumUtil;
import utils.ResponsesDTO;
import business.ItemService;
import business.proxy.IListDaoProxy;
import cache.ICacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spring/spring-config.xml" })
public class ItemBusinessTest {
	
	@Resource
	private ItemService itemService;
	
	@Resource 
	private ICacheManager cacheManager;
	
	@Resource
	private IListDaoProxy listDaoProxy;
	
	@Test
	@Ignore
	public void tt() {
		List<Object> params = new LinkedList<Object>();
		params.add("zhou");
		System.out.println(listDaoProxy.getMaxListSize("listcache.item_by_name"));
	}
	
	@Test
	@Ignore
	public void tc() {
		List<Object> params = new LinkedList<Object>();
		params.add("zhou");
		System.out.println(listDaoProxy.getLength("listcache.item_by_name", params));
	}
	
	@Test
//	@Ignore
	public void testInsert() {
		try{
			Item item = new Item();
			item.setName("zhou");
			item.setPrice(651.79);
			item.setOrderNum(OrderNumUtil.create());
			itemService.insertItem(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void testDelete() {
		try{
			Item item = new Item();
			item.setId(103);
			item.setName("zhou");
			itemService.deleteItem(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void testGet() {
		ResponsesDTO response = itemService.getItemById(1);
		
		System.out.println(((Item)response.getAttach()).getId());
		System.out.println(((Item)response.getAttach()).getName());
		System.out.println(((Item)response.getAttach()).getPrice());
	}

	@Test
	@Ignore
	public void testUpdate() {
		Item item = new Item();
		item.setId(2);
		item.setName("zhou");
		item.setPrice(100.23);
		ResponsesDTO response = itemService.update(item);
		System.out.println(response.getCode());
	}
	
	@Test
	@Ignore
	public void testGetMulti() {
		List<Integer> list = new LinkedList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		Integer ids [] = new Integer[list.size()];
		
		ResponsesDTO response = itemService.getMulti((Integer[])list.toArray(ids), true);
		List<Item> result = (List<Item>)response.getAttach();
		for(Item item : result) {
			System.out.println(item.getPrice());
		}
	}
	
	/**
	 * listDaoProxy.get(LISTCACHE_ITEM_ALL, param);
	 */
	@Test
	@Ignore
	public void getItems() {
		ResponsesDTO response = itemService.getItems("", 0, 0);
		System.out.println(response.getCode());
	}
	
	@Test
	@Ignore
	public void getItemsByName() {
		ResponsesDTO response = itemService.getItemsByName("zhou");
		System.out.println(response.getCode());
	}
	
	@Test
	@Ignore
	public void g() {
		String orderNum = "20150511172224490079";
		ResponsesDTO response = itemService.getFromOrderNum(orderNum);
		Item item = (Item)response.getAttach();
		System.out.println(item.getId());
		System.out.println(item.getName());
		System.out.println(item.getPrice());
		System.out.println(item.getOrderNum());
	}
	
}

