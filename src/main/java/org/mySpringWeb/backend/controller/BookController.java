package org.mySpringWeb.backend.controller;

import org.mySpringWeb.backend.middleware.ReturnFormat;
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
    
    /**
     * 根据图书ID获取图书
     * @param bookID
     * @return
     */
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
    		return ReturnFormat.format(temp);
    	}
    	else
    		return ReturnFormat.format(null,1,"Cannot find data.");
    }
    
    /**
     * 根据ISBN获取图书
     * @param ISBN
     * @return
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
    		return ReturnFormat.format(temp);
    	}
    	else
    		return ReturnFormat.format(null,1,"Cannot find data.");
    }
    
    /**
     * 根据一个作者获取图书列表
     * @param authorID
     * @return
     */
    @RequestMapping("/getBooksByAuthorID")
    @ResponseBody
    public Map<String, Object> getBooksByAuthorID(@RequestParam(value = "authorID") String authorID) {
    	String sql = "SELECT * FROM books WHERE author_ID LIKE ? OR author_ID LIKE ? OR author_ID LIKE ? OR author_ID LIKE ?";
    	Object[] objects = new Object[4];
    	objects[0] = "%," + authorID +",%";
    	objects[1] = authorID +",%";
    	objects[2] = "%," + authorID;
    	objects[3] = authorID;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	//return maps.get(0);
    	return ReturnFormat.format(maps);
    }
    
    /**
     * 根据一个标签获取图书列表
     * @param tagID
     * @return
     */
    @RequestMapping("/getBooksByTagID")
    @ResponseBody
    public Map<String, Object> getBooksByTagID(@RequestParam(value = "tagID") String tagID) {
    	String sql = "SELECT * FROM books WHERE label_IDs LIKE ? OR label_IDs LIKE ? OR label_IDs LIKE ? OR label_IDs LIKE ?";
    	Object[] objects = new Object[4];
    	objects[0] = "%," + tagID +",%";
    	objects[1] = tagID +",%";
    	objects[2] = "%," + tagID;
    	objects[3] = tagID;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	return ReturnFormat.format(maps);
    }
    
    
}
