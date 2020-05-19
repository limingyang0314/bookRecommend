package org.mySpringWeb.backend.controller;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.mySpringWeb.backend.middleware.ReturnFormat;
import org.mySpringWeb.backend.service.RedisService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登陆验证控制类
 * @author limingyang
 *
 */

@Controller
@RequestMapping("/api/auth")
public class AuthController{
	//redis-server.exe --service-start 
	//redis-server.exe --service-stop
	private RedisService redis = new RedisService();
	
	@Resource
    //private RedisTemplate<String, String> redisTemplate;//user_ID->authToken
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 登陆，返回token
	 * @param userName
	 * @param password
	 * @return
	 */
	public Map<String,Object> login(String userName, String password){
		password = getMD5(password);
		String sql = "SELECT * FROM users U,password P WHERE P.password = ? AND U.user_name = ?";
		Object[] arguments = {password, userName};
		if(jdbcTemplate.queryForList(sql,arguments).size() > 0) {
			//查有此人，返回token
			HashMap<String,Object> data = new HashMap<String,Object>();
			data.put("authToken",getAuthToken(userName));
			data.put("expireTime",getAuthTokenExpireTime(userName));
			return ReturnFormat.format(data);
		}
		return ReturnFormat.format(null,2,"Wrong username or password!");
	}
	
	/**
	 * 获取当前用户登陆token的过期时间
	 * @param userName
	 * @return
	 */
	public long getAuthTokenExpireTime(String userName) {
		return redis.getExpire("auth:" + userName);
	}
	
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
	public boolean isAuthTokenExpired(String userName) {
		return redis.hasKey("auth:" + userName);
	}
	
	/**
	 * md5算法
	 * @param s
	 * @return
	 */
	public String getMD5(String s) {
	        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};        
	        try {
	            byte[] btInput = s.getBytes("utf-8");
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            mdInst.update(btInput);
	            byte[] md = mdInst.digest();
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	
	

}
