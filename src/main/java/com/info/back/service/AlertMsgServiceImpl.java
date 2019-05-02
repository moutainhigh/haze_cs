package com.info.back.service;

import java.util.Date;
import java.util.HashMap;

import com.info.back.utils.IdGen;
import com.info.constant.Constant;
import com.info.back.dao.IPaginationDao;
import com.info.back.dao.ISysAlertMsgDao;
import com.info.back.result.JsonResult;
import com.info.web.pojo.cspojo.SysAlertMsg;
import com.info.web.util.PageConfig;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AlertMsgServiceImpl implements IAlertMsgService {
	
	@Resource
	private ISysAlertMsgDao sysAlertMsgDao;
	@Resource
	private IPaginationDao paginationDao;
	@Override
    public void insert(SysAlertMsg sysAlertMsg) {
		sysAlertMsg.setId(IdGen.uuid());
		sysAlertMsg.setCreateDate(new Date());
		sysAlertMsgDao.insert(sysAlertMsg);
	}

	@Override
	public PageConfig<SysAlertMsg> findPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "SysAlertMsg");
		PageConfig<SysAlertMsg> pageConfig = new PageConfig<SysAlertMsg>();
		pageConfig = paginationDao.findPage("findAll", "findAllCount", params,null);
		return pageConfig;
	}

	@Override
	public JsonResult updateTogAlertMsg(String alertId) {
		JsonResult result=new JsonResult("-1","标记为已解决失败");
		int count=sysAlertMsgDao.updateTogAlertMsg(alertId);
		if(count>0){
			result.setCode("0");
			result.setMsg("标记为已解决成功");
		}
		return result;
	}

}
