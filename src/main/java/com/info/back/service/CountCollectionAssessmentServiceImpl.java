package com.info.back.service;

import com.info.back.dao.ICountCollectionAssessmentDao;
import com.info.back.dao.IPaginationDao;
import com.info.back.utils.BackConstant;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.CountCollectionAssessment;
import com.info.web.util.DateUtil;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class CountCollectionAssessmentServiceImpl implements ICountCollectionAssessmentService {

    @Resource
    private ICountCollectionAssessmentDao countCollectionAssessmentDao;
    @Resource
    private IPaginationDao paginationDao;

    @Override
    public PageConfig<CountCollectionAssessment> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "CountCollectionAssessment");
        PageConfig<CountCollectionAssessment> pageConfig = new PageConfig<CountCollectionAssessment>();
        String listSql = "findAllByCompany";
        String countSql = "findAllCountByCompany";
        if ("MR".equals(params.get("type"))) {
            listSql = "findAll";
            countSql = "findAllCount";
            pageConfig = paginationDao.findPage(listSql, countSql, params, null);
        } else if ("LJ".equals(params.get("type"))) {
            listSql = "findAllByGroup";
            countSql = "findAllCountByGroup";
            pageConfig = paginationDao.findPage(listSql, countSql, params, null);
            this.handleTotalData(pageConfig.getItems());
        } else {
            // 统计累计 公司
            pageConfig = paginationDao.findPage(listSql, countSql, params, null);
            this.handleTotalData(pageConfig.getItems());
        }
        if (!"C".equals(params.get("method"))) {   //collection层的方法为统计时不需要计算总数
            if (pageConfig != null && CollectionUtils.isNotEmpty(pageConfig.getItems())) {
                params.put("numPerPage", pageConfig.getTotalResultSize());

                PageConfig<CountCollectionAssessment> pageConfigAll = paginationDao.findPage(listSql, countSql, params, null);
                CountCollectionAssessment cca = this.handleData(pageConfigAll.getItems());
                if (cca != null) {
                    pageConfig.getItems().add(0, cca);
                }
            }
//			this.handleData(pageConfig.getItems());
        }
        return pageConfig;
    }

    @Override
    public List<CountCollectionAssessment> findAll(HashMap<String, Object> params) {
        List<CountCollectionAssessment> list = null;
        if ("MR".equals(params.get("type"))) {
            list = countCollectionAssessmentDao.findAll(params);
        } else {
            list = countCollectionAssessmentDao.findAllByGroup(params);
            this.handleTotalData(list);
        }
        return list;
    }

    @Override
    public Integer findAllCount(HashMap<String, Object> params) {
        if ("MR".equals(params.get("type"))) {
            return countCollectionAssessmentDao.findAllCount(params);
        } else {
            return countCollectionAssessmentDao.findAllCountByGroup(params);
        }
    }

    @Override
    public CountCollectionAssessment getOne(HashMap<String, Object> params) {
        List<CountCollectionAssessment> list = this.findAll(params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 数据处理
     *
     * @param list
     */
    private void handleTotalData(List<CountCollectionAssessment> list) {
        if (list != null && list.size() > 0) {
            for (CountCollectionAssessment ca : list) {
                if (ca.getLoanMoney().compareTo(new BigDecimal(0)) == 1) {
                    ca.setRepaymentReta(ca.getRepaymentMoney().divide(ca.getLoanMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (ca.getPenalty().compareTo(new BigDecimal(0)) == 1) {  //滞纳金还款率
                    ca.setPenaltyRepaymentReta(ca.getRepaymentPenalty().divide(ca.getPenalty(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (ca.getOrderTotal() > 0) {   //订单还款率
                    double rate = ((double) ca.getRepaymentOrderNum()) / ca.getOrderTotal() * 100;
                    ca.setRepaymentOrderRate(new BigDecimal(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (!BackConstant.XJX_OVERDUE_LEVEL_S1.equals(ca.getGroupId()) && !BackConstant.XJX_OVERDUE_LEVEL_S2.equals(ca.getGroupId())) {
                    if (null != ca.getRepaymentReta()) {
                        ca.setMigrateRate(new BigDecimal(100).subtract(ca.getRepaymentReta()));
                    }
                } else {
                    ca.setMigrateRate(new BigDecimal(-1.00).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }

    /**
     * 数据处理
     *
     * @param list
     */
    private CountCollectionAssessment handleData(List<CountCollectionAssessment> list) {
        CountCollectionAssessment cc = new CountCollectionAssessment();
        if (list != null && list.size() > 0) {
            for (CountCollectionAssessment ca : list) {
                cc.setLoanMoney(cc.getLoanMoney().add(ca.getLoanMoney()));   //本金
                cc.setRepaymentMoney(cc.getRepaymentMoney().add(ca.getRepaymentMoney()));  //已还本金
                cc.setNotYetRepaymentMoney(cc.getNotYetRepaymentMoney().add(ca.getNotYetRepaymentMoney()));   //未还本金
                cc.setPenalty(cc.getPenalty().add(ca.getPenalty()));   //滞纳金
                cc.setRepaymentPenalty(cc.getRepaymentPenalty().add(ca.getRepaymentPenalty()));   //已还滞纳金
                cc.setNotRepaymentPenalty(cc.getNotRepaymentPenalty().add(ca.getNotRepaymentPenalty()));   //未还滞纳金
                cc.setOrderTotal(cc.getOrderTotal() + ca.getOrderTotal());   // 订单量
                cc.setRepaymentOrderNum(cc.getRepaymentOrderNum() + ca.getRepaymentOrderNum());  //已还订单量
                cc.setDisposeOrderNum(cc.getDisposeOrderNum() + ca.getDisposeOrderNum());  //已操作订单量
                cc.setRiskOrderNum(cc.getRiskOrderNum() + ca.getRiskOrderNum());   //风控标记单量
            }
            BigDecimal rr = new BigDecimal(0);     //本金还款率
            BigDecimal rpr = new BigDecimal(0);    //滞纳金还款率
            BigDecimal ror = new BigDecimal(0);    //订单还款率
            if (cc.getLoanMoney().compareTo(new BigDecimal(0)) == 1) {
                rr = cc.getRepaymentMoney().divide(cc.getLoanMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

            }
            if (cc.getPenalty().compareTo(new BigDecimal(0)) == 1) {  //滞纳金还款率
                rpr = cc.getRepaymentPenalty().divide(cc.getPenalty(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (cc.getOrderTotal() > 0) {   //订单还款率
                ror = new BigDecimal(cc.getRepaymentOrderNum()).divide(new BigDecimal(cc.getOrderTotal()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//	    		double rate = cc.getRepaymentOrderNum() / cc.getOrderTotal() * 100d;
//	    		ror = new BigDecimal(rate).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            cc.setRepaymentReta(rr);
            cc.setMigrateRate(new BigDecimal(-1.00).setScale(2, BigDecimal.ROUND_HALF_UP));
            cc.setPenaltyRepaymentReta(rpr);
            cc.setRepaymentOrderRate(ror);
//	    	list.add(0, cc);
        }
        return cc;
    }


    @Override
    public void countCallAssessment(HashMap<String, Object> params) {
//		countCollectionAssessmentDao.callAssessment(params);
        String begDate = String.valueOf(params.get("begDate"));
        String endDate = String.valueOf(params.get("endDate"));
        log.info("begDate: {} | endDate: {}", begDate, endDate);
        int count = 0;
        try {
            count = DateUtil.daysBetween(DateUtil.formatDate(begDate, "yyyy-MM-dd"), DateUtil.formatDate(endDate, "yyyy-MM-dd"));
        } catch (ParseException e) {
            log.error("计算时间天数异常", e);
        }

        for (int i = 0; i < count; i++) {
            params.put("currDate", DateUtil.getBeforeOrAfter(DateUtil.getDateTimeFormat(begDate, "yyyy-MM-dd"), i));
            List<CountCollectionAssessment> examineList = countCollectionAssessmentDao.queryExamineList(params);
            if (CollectionUtils.isNotEmpty(examineList)) {
                countCollectionAssessmentDao.insertExamineList(examineList);
            }
        }
    }

    @Override
    public void callMGroupAssessment(HashMap<String, Object> params) {
        // TODO Auto-generated method stub
        countCollectionAssessmentDao.callMGroupAssessment(params);
    }

}
