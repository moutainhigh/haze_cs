package com.info.back.dao;

import java.util.HashMap;
import java.util.List;

import com.info.web.pojo.cspojo.MmanLoanCollectionStatusChangeLog;
import org.springframework.stereotype.Repository;

@Repository
public interface IMmanLoanCollectionStatusChangeLogDao {
	/**
	 * 查询所有催收流转日志记录
	 * 
	 * @return 查询到的催收流转日志记录
	 */
	public List<MmanLoanCollectionStatusChangeLog> findAll(HashMap<String, Object> params);
	
	/**
	 * 新增 MmanLoanCollectionStatusChangeLog
	 * @param mmanLoanCollectionStatusChangeLog
	 */
	public void insert(MmanLoanCollectionStatusChangeLog  mmanLoanCollectionStatusChangeLog);
	
	
	public List<MmanLoanCollectionStatusChangeLog> findListLog(String orderId);

	public int findOrderSingle(HashMap<String, String> params);
	
	
	public Integer findSystemUpToS2Log(HashMap<String, String> params);

	List<String> getOrderIds(HashMap<String, Object> params);

	Integer getOrderIdNum();

	List<MmanLoanCollectionStatusChangeLog> getLogByOrderIds(List<String> ids);
}
