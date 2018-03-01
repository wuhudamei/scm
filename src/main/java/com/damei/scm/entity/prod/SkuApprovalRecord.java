package com.damei.scm.entity.prod;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.damei.scm.common.IdEntity;
import com.damei.scm.common.utils.DateUtil;

public class SkuApprovalRecord extends IdEntity {


    private String approvalAccount;


    private String approvalResult;


    private String approvalNote;


    private Long skuId;

    private String approvalNode;


    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date approvalTime;

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