package com.damei.scm.entity.order;

import com.damei.scm.common.IdEntity;
import com.damei.scm.entity.eum.PayStatusEnum;
import com.damei.scm.entity.eum.SendStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

public class ReconciliationOrderItem extends IdEntity {

    private Long supplierId;

    private Long skuId;

    private String skuName;

    private String model;

    private String spec;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private BigDecimal supplyPrice;

    private BigDecimal quantity;

    private BigDecimal otherFee;

    private SendStatusEnum sendStatus;

    private Date sendTime;

    private PayStatusEnum payStatus;

    private BigDecimal totalMoney;

    private String contractCode;

    private String orderCode;

    private Date payTime;

    private String operator;

    private String operatName;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public SendStatusEnum getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatusEnum sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    public BigDecimal getTotalMoney() {
        if(this.otherFee == null){
            totalMoney = this.quantity.multiply(this.supplyPrice);
        }else {
            totalMoney = this.quantity.multiply(this.supplyPrice).add(this.otherFee);
        }
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOperatName() {
        return operatName;
    }

    public void setOperatName(String operatName) {
        this.operatName = operatName;
    }
}
