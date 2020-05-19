package org.mySpringWeb.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 封装的JDBC
 * @author limingyang
 *
 */
public class JDBCService {
	@Resource
    private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取所有数据
	 * @param sql
	 * @param ref
	 * @return
	 */
	public List<Map<String,Object>> selectAll(String sql, Object[] ref){
		return jdbcTemplate.queryForList(sql,ref);
	}
	
	/**
	 * 只获取一行
	 * @param sql
	 * @param ref
	 * @return
	 */
	public Map<String,Object> selectOneRow(String sql, Object[] ref){
		List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql,ref);
		if(maps.size() > 0) {
			return maps.get(0);
		}else {
			return new HashMap<String,Object>();
		}
	}
	
	/**
	 * 插入一行,我还没研究返回值怎么处理比较妥当所以就先void了
	 * @param sql
	 * @param ref
	 */
	public void insertOneRow(String sql, Object[] ref) {
		jdbcTemplate.update(sql,ref);
	}
	
	/**
	 * 插入多行
	 * @param sql
	 * @param refs
	 */
	public void insertAll(String sql, Object[][] refs) {
		for(Object[] ref : refs) {
			jdbcTemplate.update(sql,ref);
		}
	}
	
//	public Integer count(String sql, Object[] ref) {
//		return jdbcTemplate.queryForInt();
//	}

}
