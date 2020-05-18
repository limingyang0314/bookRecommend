package org.mySpringWeb.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mySpringWeb.backend.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 图书控制类
 * @date 2020/5/18 10:10
 */

@Controller
@RequestMapping("/api/book")
public class BookController {

    @Resource
    private JdbcTemplate jdbcTemplate;
    

    @RequestMapping("/getBookByBookID")
    @ResponseBody
    public List<Book> getBookByBookID(ModelMap map) {
    	return new ArrayList<Book>();
    }
    
    @RequestMapping("/getBookByISBN")
    @ResponseBody
    public List<Book> getBookByISBN(@RequestParam(value = "ISBN") String ISBN) {
    	String sql = "SELECT * FROM books WHERE ISBN = ?";
    	Object[] objects = new Object[1];
    	objects[0] = ISBN;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	List<Book> res = new ArrayList<Book>();
    	for(Map<String, Object> m:maps) {
    		System.out.println(m);
    	}
    	return res;
    }
    
    @RequestMapping("/getBooksByAuthorID")
    @ResponseBody
    public List<Book> getBooksByAuthorID(ModelMap map) {
    	return new ArrayList<Book>();
    }
    
    @RequestMapping("/getBooksByTagID")
    @ResponseBody
    public List<Book> getBooksByTagID(ModelMap map) {
    	return new ArrayList<Book>();
    }
    
    
}
