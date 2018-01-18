package com.mdni.scm.entity.prod;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;

/**
 * @describe sku审批记录 实体
 * @author Ryze
 * @date 2017-8-14 18:53:29
 *
 */
public class SkuApprovalRecord extends IdEntity {

    /**
     * 审批人账号
     */
    private String approvalAccount;

    /**
     * 审批结果
     */
    private String approvalResult;

    /**
     * 审批说明
     */
    private String approvalNote;

    /**
     * sku 外键
     */
    private Long skuId;
    /**
     * sku node
     */
    private String approvalNode;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date approvalTime;

    /**
     * 操作人名字
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static final long serialVersionUID = 1L;

    public String getApprovalNode() {
        return approvalNode;
    }

    public void setApprovalNode(String approvalNode) {
        this.approvalNode = approvalNode;
    }

    public String getApprovalAccount() {
        return approvalAccount;
    }

    public void setApprovalAccount(String approvalAccount) {
        this.approvalAccount = approvalAccount;
    }

    public String getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getApprovalNote() {
        return approvalNote;
    }

    public void setApprovalNote(String approvalNote) {
        this.approvalNote = approvalNote;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }
}