package org.mySpringWeb.backend.controller;

import javax.annotation.Resource;

import org.mySpringWeb.backend.service.RedisService;
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
	private RedisService redis = new RedisService();
	
	@Resource
    //private RedisTemplate<String, String> redisTemplate;//user_ID->authToken
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取登陆验证token,过期才会获取新的
	 * @param userName
	 * @return
	 */
	public String getAuthToken(String userName) {
		if(redis.hasKey("auth:" + userName)) {
			//token已经存在,返回
			return (String)redis.get("auth:" + userName);
		}else {
			//说明过期
			
		}
		return null;
	}
	
	/**
	 * 判断登陆token是否过期
	 * @param userName
	 * @return
	 */
	private boolean isAuthTokenExpired(String userName) {
		return redis.hasKey("auth:" + userName);
	}
	
	

}
