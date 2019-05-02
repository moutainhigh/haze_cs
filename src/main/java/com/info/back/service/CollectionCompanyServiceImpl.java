package com.info.back.service;

import com.info.back.dao.IMmanLoanCollectionCompanyDao;
import com.info.back.dao.IMmanLoanCollectionRuleDao;
import com.info.back.dao.IPaginationDao;
import com.info.back.dao.ISysDictDao;
import com.info.back.result.JsonResult;
import com.info.back.utils.IdGen;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackUser;
import com.info.web.pojo.cspojo.MmanLoanCollectionCompany;
import com.info.web.pojo.cspojo.MmanLoanCollectionRule;
import com.info.web.pojo.cspojo.SysDict;
import com.info.web.util.PageConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CollectionCompanyServiceImpl implements ICollectionCompanyService {
    @Resource
    private IPaginationDao paginationDao;
    @Resource
    private IMmanLoanCollectionCompanyDao mmanLoanCollectionCompanyDao;
    @Resource
    private IMmanLoanCollectionRuleDao mmanLoanCollectionRuleDao;
    @Resource
    private ISysDictDao sysDictDao;

    @Override
    public PageConfig<MmanLoanCollectionCompany> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "MmanLoanCollectionCompany");
        PageConfig<MmanLoanCollectionCompany> pageConfig;
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, null);
        return pageConfig;
    }

    @Override
    public JsonResult saveCompany(Map<String, String> params) {
        JsonResult result = new JsonResult("-1", "添加公司失败");
        MmanLoanCollectionCompany mmanLoanCollectionCompany = new MmanLoanCollectionCompany();
        if (StringUtils.isNotBlank(params.get("title"))) {
            if (params.get("status") == null || params.get("status") == "") {
                mmanLoanCollectionCompany.setStatus("1");
            } else {
                mmanLoanCollectionCompany.setStatus(params.get("status"));
            }
            if (params.get("selfBusiness") == null || params.get("selfBusiness") == "") {
                mmanLoanCollectionCompany.setSelfBusiness("1");
            } else {
                mmanLoanCollectionCompany.setSelfBusiness(params.get("selfBusiness"));
            }
            if (params.get("priority") == null || params.get("priority") == "") {
                mmanLoanCollectionCompany.setPriority("1");
            } else {
                mmanLoanCollectionCompany.setPriority(params.get("priority"));
            }
            mmanLoanCollectionCompany.setTitle(params.get("title"));
            mmanLoanCollectionCompany.setRegion(params.get("region"));
            mmanLoanCollectionCompany.setId(IdGen.uuid());
            mmanLoanCollectionCompany.setCreateDate(new Date());
            if (mmanLoanCollectionCompanyDao.insert(mmanLoanCollectionCompany) > 0) {
                result.setCode("0");
                result.setMsg("添加公司成功");
                HashMap<String, Object> ruleParams = new HashMap<>();
                ruleParams.put("id", mmanLoanCollectionCompany.getId());
                //查询催收组
                List<SysDict> dictlist = sysDictDao.getStatus("collection_group");
                for (SysDict dict : dictlist) {
                    MmanLoanCollectionRule mmanLoanCollectionRule = new MmanLoanCollectionRule();
                    mmanLoanCollectionRule.setId(IdGen.uuid());
                    mmanLoanCollectionRule.setCompanyId(mmanLoanCollectionCompany.getId());
                    mmanLoanCollectionRule.setCollectionGroup(dict.getValue());
                    mmanLoanCollectionRule.setEveryLimit(0);
                    mmanLoanCollectionRuleDao.insert(mmanLoanCollectionRule);
                }
            }
        } else {
            result.setMsg("公司名称不能为空");
        }
        return result;
    }

    @Override
    public JsonResult updateCompany(Map<String, String> params) {
        JsonResult result = new JsonResult("-1", "修改公司失败");
        MmanLoanCollectionCompany mmanLoanCollectionCompany = new MmanLoanCollectionCompany();
        if (StringUtils.isNotBlank(params.get("title")) && StringUtils.isNotBlank(params.get("id"))) {
            if (params.get("status") == null || params.get("status") == "") {
                mmanLoanCollectionCompany.setStatus("1");
            } else {
                mmanLoanCollectionCompany.setStatus(params.get("status"));
            }
            if (params.get("selfBusiness") == null || params.get("selfBusiness") == "") {
                mmanLoanCollectionCompany.setSelfBusiness("1");
            } else {
                mmanLoanCollectionCompany.setSelfBusiness(params.get("selfBusiness"));
            }
            if (params.get("priority") == null || params.get("priority") == "") {
                mmanLoanCollectionCompany.setPriority("1");
            } else {
                mmanLoanCollectionCompany.setPriority(params.get("priority"));
            }
            mmanLoanCollectionCompany.setTitle(params.get("title"));
            mmanLoanCollectionCompany.setId(params.get("id"));
            mmanLoanCollectionCompany.setUpdateDate(new Date());
            mmanLoanCollectionCompany.setRegion(params.get("region"));
            int status = 1;
            if ("0".equals(mmanLoanCollectionCompany.getStatus())) {
                int count = mmanLoanCollectionCompanyDao.findcomapyIdOrder(params.get("id"));
                if (count <= 0) {
                    if (mmanLoanCollectionCompanyDao.update(mmanLoanCollectionCompany) > 0) {
                        result.setCode("0");
                        result.setMsg("修改公司信息成功");
                    }
                } else {
                    result.setMsg("该公司还有未完成的订单,等订单完成后或者转派后才能禁用");
                }
                status = 0;
            } else {
                if (mmanLoanCollectionCompanyDao.update(mmanLoanCollectionCompany) > 0) {
                    result.setCode("0");
                    result.setMsg("修改公司信息成功");
                }
            }
            List<BackUser> backUserList = mmanLoanCollectionCompanyDao.findcomapyIdUser(params.get("id"));
            List<String> uuidlist = new ArrayList<>();
            if (backUserList != null) {
                for (BackUser user : backUserList) {
                    uuidlist.add(user.getUuid());
                }
            }
            if (backUserList != null && backUserList.size() > 0) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("UUIdlist", uuidlist);
                map.put("status", status);
                //删除催收员
                mmanLoanCollectionCompanyDao.delUser(map);
            }
        } else {
            result.setMsg("参数不正确");
        }
        return result;
    }

    @Override
    public JsonResult deleteCompany(String compayId) {
        JsonResult result = new JsonResult("-1", "删除公司失败");
        try {
            if (!"1".equals(compayId)) {
                int count = mmanLoanCollectionCompanyDao.findcomapyIdOrder(compayId);
                if (count <= 0) {
                    List<BackUser> backUserList = mmanLoanCollectionCompanyDao.findcomapyIdUser(compayId);
                    List<String> uuidlist = new ArrayList<>();
                    if (backUserList != null) {
                        for (BackUser user : backUserList) {
                            uuidlist.add(user.getUuid());
                        }
                    }
                    if (backUserList != null && backUserList.size() > 0) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("UUIdlist", uuidlist);
                        map.put("status", 3);
                        //删除催收员
                        mmanLoanCollectionCompanyDao.delUser(map);
                        //标记订单为需要删除
                        mmanLoanCollectionCompanyDao.updateOrderStatus(map);
                    }
                    int delCount = mmanLoanCollectionCompanyDao.del(compayId);
                    if (delCount > 0) {
                        result.setCode("0");
                        result.setMsg("删除公司成功");
                    }
                } else {
                    result.setMsg("该公司还有未完成的订单,等订单完成后或者转派后才能删除");
                }
            } else {
                result.setMsg("改公司为自己公司不能删除");
            }
        } catch (Exception e) {
            result.setMsg("系统异常");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public MmanLoanCollectionCompany get(String id) {
        return mmanLoanCollectionCompanyDao.get(id);
    }


    @Override
    public List<MmanLoanCollectionCompany> selectCompanyList() {
        return mmanLoanCollectionCompanyDao.selectCompanyList();
    }
}
