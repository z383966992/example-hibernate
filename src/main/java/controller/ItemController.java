package controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Item;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import business.ItemService;
import utils.Page;
import utils.ResponsesDTO;
import utils.ReturnCode;
import constants.Constants;

@Controller
@RequestMapping(value="/item")
public class ItemController {
	
	@Resource
	private ItemService itemService;
	
	private static Logger logger = Logger.getLogger(ItemController.class);
	
	/**
	 * 返回json数据格式
	 * @param request
	 * @param reponse
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list/json")
	public ResponsesDTO listItemJson(HttpServletRequest request, HttpServletResponse response) {
		String currentPage = request.getParameter("currentPage");
		ResponsesDTO result = itemService.getItems("hahah", Constants.DEFALUT_PAGE_SIZE, Integer.valueOf(currentPage));
		
		ResponsesDTO resp = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
		
		if (result.getCode() == ReturnCode.ACTIVE_SUCCESS.code()) {
			Page<Item> page = result.getAttach();
			resp.setAttach(page.getContentList());
			resp.setReturnCode(ReturnCode.ACTIVE_SUCCESS);
		} 
		return resp;
	}
	
	/**
	 * 分页，迭代list，map，普通对象
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list/paging")
	public ModelAndView listItemPaging(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String currentPage = request.getParameter("currentPage");
		
		
		ModelAndView mv = new ModelAndView();
		ResponsesDTO result = itemService.getItems("hahah", Constants.DEFALUT_PAGE_SIZE, Integer.valueOf(currentPage));
		if (result.getCode() == ReturnCode.ACTIVE_SUCCESS.code()) {
			Page<List<Item>> page = result.getAttach();
			
			//map数据
			Map<String,String> map = new HashMap<String, String>();
			map.put("123", "321");
			map.put("456", "654");
			map.put("789", "987");
			mv.addObject("map", map);
			
			//普通对象
			mv.addObject("test", "test");
			
			//Date数据
			mv.addObject("date", new Date());
			
			mv.addObject("page", page);
			mv.setViewName("item/item");
		} else {
			mv.setViewName("error/error");
		}
		
		return mv;
	}
	
	/**
	 * 插入并测试事务
	 * transactional test
	 * @param request
	 * @param reponse
	 * @return
	 */
	@RequestMapping(value="/insert/item")
	public ModelAndView insertItem(HttpServletRequest request, HttpServletResponse reponse) throws Exception{
		ModelAndView mv = new ModelAndView();
		Item item = new Item("hahah",456.125);
		itemService.insertItem(item);
		mv.setViewName("item/success");
		return mv;
	}
}



















