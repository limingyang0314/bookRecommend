package org.mySpringWeb.backend.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 浏览相关控制器
 * @author limingyang
 *
 */

@Controller
@RequestMapping("/api/browsing")
public class BrowsingController {
	/**
	 * 记录用户访问
	 * @param userName
	 * @param bookID
	 * @return
	 */
	public Map<String,Object> recordBrowsing(String userID, String bookID){
		return null;
	}

}
