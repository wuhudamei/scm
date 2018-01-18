package com.mdni.scm.entity.operateLog;

import com.mdni.scm.common.IdEntity;

import java.util.Date;

/**
 * Created by 刘铎 on 2017/7/26.
 */
public class OperateLog extends IdEntity{

    private String operator;//操作人
    private Date operatorTime;//操作时间
    private String operatorExplain;//操作说明
    private String orderId;//订单id
    private String contractCode;//项目编号

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
