package com.damei.scm.entity.operateLog;

import com.damei.scm.common.IdEntity;

import java.util.Date;

public class OperateLog extends IdEntity{

    private String operator;
    private Date operatorTime;
    private String operatorExplain;
    private String orderId;
    private String contractCode;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public String getOperatorExplain() {
        return operatorExplain;
    }

    public void setOperatorExplain(String operatorExplain) {
        this.operatorExplain = operatorExplain;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
}
