package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 审核表
 */
@Data
public class AuditCenter {
    /**
     * 序列号
     */
    private String id;
    /**
     * 操作用户编号
     */
    private String operationUserId;
    /**
     * 借款用户编号
     */
    private String loanUserId;
    private String loanId;
    /**
     * 还款id
     */
    private String payId;
    /**
     * 催收订单编号
     */
    private String orderid;
    /**
     * 减免滞纳金
     */
    private Long reductionMoney;
    /**
     * 0:聚信立，1 催收建议 3订单减免
     */
    private String type;
    /**
     * 状态 0 申请中，2审核通过 3，拒绝 ,4失效 5,通过不计入考核
     */
    //
    private String status;
    /**
     * 审核时间
     */
    private Date audittime;
    private Date createtime;
    private Date updatetime;
    /**
     * 标签
     */
    private String note;
    /**
     * 逾期标签
     */
    private String labelValue;
    /**
     * 借款表 滞纳金
     */
    private BigDecimal loanPenalty;
    /**
     * 借款人姓名
     */
    private String loanUserName;
    /**
     * 借款人电话
     */
    private String loanUserPhone;
    private String remark;
    /**
     * 订单状态(mman_loan_collection_order 的status状态，用于减免时状态同步)
     */
    private String orderStatus;
    /**
     * 申请人与表无关
     */
    private String userName;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 催收状态
     */
    private String collectionStatus;

}