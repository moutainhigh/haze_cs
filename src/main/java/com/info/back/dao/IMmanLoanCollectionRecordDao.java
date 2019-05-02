package com.info.back.dao;

import java.util.HashMap;
import java.util.List;

import com.info.web.pojo.cspojo.MmanLoanCollectionRecord;
import org.springframework.stereotype.Repository;

/**
 * 催收记录dao层
 * 
 * @author hxj
 * 
 */
@Repository
public interface IMmanLoanCollectionRecordDao {
	/**
	 * 查询所有催收记录
	 * @return 查询到的所有催收记录
	 */
	public List<MmanLoanCollectionRecord> findAll(HashMap<String, Object> params);
	
	/**
	 * 获取一条催收记录 
	 * @param id 要查询的记录id
	 * @return 查询到的记录对象
	 */
	public MmanLoanCollectionRecord getOneCollectionRecord(Integer id);
	
	/**
	 * 添加一条催收记录
	 */
	public void insert(MmanLoanCollectionRecord record);
	
	/**
	 * 更新一条记录
	 */
	public void update(MmanLoanCollectionRecord record);
	
	/**
	 * 根据订单号查询历史
	 * @return
	 */
	public List<MmanLoanCollectionRecord> findListRecord(String orderid);
}
