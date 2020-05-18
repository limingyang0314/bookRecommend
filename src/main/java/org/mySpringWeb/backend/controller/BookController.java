package org.mySpringWeb.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 图书控制类
 * @author limingyang
 *
 */

@Controller
@RequestMapping("/api/book")
public class BookController {

    @Resource
    private JdbcTemplate jdbcTemplate;
    

    @RequestMapping("/getBookByBookID/{id}")
    @ResponseBody
    public Map<String, Object> getBookByBookID(@PathVariable("id") String bookID) {
    	String sql = "SELECT B.*,C.country_name FROM books B,countries C WHERE C.country_ID = B.country_ID AND B.book_ID = ?";
    	Object[] objects = new Object[1];
    	objects[0] = bookID;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	if(maps.size() > 0) {
    		Map<String,Object> temp = maps.get(0);
    		//处理标签
    		String tag_sql = "SELECT label_name FROM labels WHERE label_ID IN (" + temp.get("label_IDs") + ")";
    		System.out.println(tag_sql);
    		List<Map<String, Object>> labels = jdbcTemplate.queryForList(tag_sql);
    		temp.put("labels","");
    		for(Map<String, Object> m : labels) {
    			if(temp.get("labels") != "")
    				temp.put("labels",temp.get("labels") + "," + m.get("label_name"));
    			else
    				temp.put("labels",m.get("label_name"));
    		}
    		return temp;
    	}
    	else
    		return new HashMap<String, Object>();
    }
    
    /*
     * 根据ISBN获取图书
     */
    @GetMapping("/getBookByISBN/{id}")
    @ResponseBody
    public Map<String, Object> getBookByISBN(@PathVariable("id") String ISBN) {
    	String sql = "SELECT * FROM books WHERE ISBN = ?";
    	Object[] objects = new Object[1];
    	objects[0] = ISBN;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	if(maps.size() > 0){
    		Map<String,Object> temp = maps.get(0);
    		//处理标签
    		String tag_sql = "SELECT label_name FROM labels WHERE label_ID IN (" + temp.get("label_IDs") + ")";
    		System.out.println(tag_sql);
    		List<Map<String, Object>> labels = jdbcTemplate.queryForList(tag_sql);
    		temp.put("labels","");
    		for(Map<String, Object> m : labels) {
    			if(temp.get("labels") != "")
    				temp.put("labels",temp.get("labels") + "," + m.get("label_name"));
    			else
    				temp.put("labels",m.get("label_name"));
    		}
    		return temp;
    	}
    	else
    		return new HashMap<String, Object>();
    }
    
    /*
     * 根据一个作者获取图书列表
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
    	String sql = "SELECT * FROM books WHERE label_IDs LIKE ? OR label_IDs LIKE ? OR label_IDs LIKE ? OR label_IDs LIKE ?";
    	Object[] objects = new Object[4];
    	objects[0] = "%," + tagID +",%";
    	objects[1] = tagID +",%";
    	objects[2] = "%," + tagID;
    	objects[3] = tagID;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	return maps;
    }
    
    
}
