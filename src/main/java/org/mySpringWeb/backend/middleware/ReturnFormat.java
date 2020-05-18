package org.mySpringWeb.backend.middleware;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对返回值进行格式化处理
 * @author limingyang
 *
 */
public class ReturnFormat {
	/**
	 * 不传errorcode和message，意味着正确
	 * @param data
	 * @return
	 */
	public static Map<String,Object> format(Object data){
		LinkedHashMap<String,Object> ret = new LinkedHashMap<String,Object>();
		ret.put("error_code", 0);
		ret.put("message", "Success.");
		ret.put("data", data);
		return ret;
	}
	
	/**
	 * 不传message
	 * @param data
	 * @param errorCode
	 * @return
	 */
	public static Map<String,Object> format(Object data, Integer errorCode){
		LinkedHashMap<String,Object> ret = new LinkedHashMap<String,Object>();
		ret.put("error_code", errorCode);
		ret.put("message", "Default failed.");
		ret.put("data", data);
		return ret;
	}
	
	/**
	 * 参数全齐
	 * @param data
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static Map<String,Object> format(Object data, Integer errorCode, String message){
		LinkedHashMap<String,Object> ret = new LinkedHashMap<String,Object>();
		ret.put("error_code", errorCode);
		ret.put("message", message);
		ret.put("data", data);
		return ret;
	}

}
