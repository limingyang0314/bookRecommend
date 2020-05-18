package org.mySpringWeb.backend.controller;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 登陆验证控制类
 * @author limingyang
 *
 */
public class AuthController {
	//redis-server.exe --service-start 
	//redis-server.exe --service-stop
	
	@Resource
    private RedisTemplate<String, String> redisTemplate;//user_ID->authToken
	private JdbcTemplate jdbcTemplate;

}
