package business.test;

import java.util.List;

import javax.annotation.Resource;

import model.Item;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import business.ItemService;
import utils.ResponsesDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spring/spring-config.xml" })
public class ServiceTest {

	@Resource
	private ItemService itemService;
	
	@Test
	public void test(){
		ResponsesDTO response = itemService.getItems("zhou", 3, 1);
		List<Item> result = response.getAttach();
		for(Item item : result) {
			System.out.println(item.getId());
			System.out.println(item.getName());
			System.out.println(item.getPrice());
			System.out.println();
		}
	}
}
