package com.info.back.dao;

import java.util.List;

import com.info.web.pojo.cspojo.TemplateSms;
import org.springframework.stereotype.Repository;

@Repository
public interface ISmsDao {
	/**
	 * 根据id查询短信模板
	 * @param id
	 * @return
	 */
	TemplateSms getTemplateSmsById(String id);
	/**
	 * 更新短信模板
	 * @param templateSms
	 * @return
	 */
	int update(TemplateSms templateSms);
	/**
	 * 添加短信模板
	 * @param templateSms
	 * @return
	 */
	int insert(TemplateSms templateSms);
	/**
	 * 删除短信模板
	 * @param id
	 * @return
	 */
	int delete(String id);
	
	/**
	 * 查询所有的短信模板
	 * @return
	 */
	List<TemplateSms> getMsgs();

}
