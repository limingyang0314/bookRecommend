package org.mySpringWeb.backend.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mySpringWeb.backend.middleware.ReturnFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 用户控制类
 * @date 2020/5/18 14:51
 */

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private JdbcTemplate jdbcTemplate;
    

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getUserInfo(@PathVariable("id") String userID) {
    	String sql = "SELECT * FROM users WHERE user_ID = ?";
    	Object[] objects = new Object[1];
    	objects[0] = userID;
    	List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, objects);
    	if(maps.size() > 0)
    		return ReturnFormat.format(maps.get(0));
    	else
    		return ReturnFormat.format(null,1,"Cannot find data.");
    }

}
