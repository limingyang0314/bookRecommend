package org.mySpringWeb.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
    
    /*
     * 根据ISBN获取图书
     */
    @RequestMapping("/getBookByISBN")
    @ResponseBody
    public Map<String, Object> getBookByISBN(@RequestParam(value = "ISBN") String ISBN) {
    	String sql = "SELECT * FROM books WHERE ISBN = ?";
    	Object[] objects = new Object[1];
    	objects[0] = ISBN;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	if(maps.size() > 0)
    		return maps.get(0);
    	else
    		return new HashMap<String, Object>();
    }
    
    /*
     * 根据一个标签获取图书列表
     */
    @RequestMapping("/getBooksByAuthorID")
    @ResponseBody
    public List<Map<String, Object>> getBooksByAuthorID(@RequestParam(value = "authorID") String authorID) {
    	String sql = "SELECT * FROM books WHERE author_ID LIKE ? OR author_ID LIKE ? OR author_ID LIKE ? OR author_ID LIKE ?";
    	Object[] objects = new Object[4];
    	objects[0] = "%," + authorID +",%";
    	objects[1] = authorID +",%";
    	objects[2] = "%," + authorID;
    	objects[3] = authorID;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	//return maps.get(0);
    	return maps;
    }
    
    /*
     * 根据一个标签获取图书列表
     */
    @RequestMapping("/getBooksByTagID")
    @ResponseBody
    public List<Map<String, Object>> getBooksByTagID(@RequestParam(value = "tagID") String tagID) {
    	String sql = "SELECT * FROM books WHERE tag_IDs LIKE ? OR tag_IDs LIKE ? OR tag_IDs LIKE ? OR tag_ID LIKE ?";
    	Object[] objects = new Object[4];
    	objects[0] = "%," + tagID +",%";
    	objects[1] = tagID +",%";
    	objects[2] = "%," + tagID;
    	objects[3] = tagID;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	return maps;
    }
    
    
}
