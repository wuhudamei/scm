package com.mdni.scm.entity.order;

import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.eum.PayStatusEnum;
import com.mdni.scm.entity.eum.SendStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <dl>
 * <dd>描述: 订货单对账详细</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年7月12日</dd>
 * <dd>创建人： Chaos/dd>
 * </dl>
 */
public class ReconciliationOrderItem extends IdEntity {
    /**供货商id**/
    private Long supplierId;
    /**商品skuid**/
    private Long skuId;
    /**商品sku名称**/
    private String skuName;
    /**商品型号**/
    private String model;
    /**商品规格**/
    private String spec;
    /**商品属性1**/
    private String attribute1;
    /**商品属性2**/
    private String attribute2;
    /**商品属性3**/
    private String attribute3;
    /**供货商的供货价**/
    private BigDecimal supplyPrice;
    /**订货数量**/
    private BigDecimal quantity;
    /**其他费用**/
    private BigDecimal otherFee;
    /**发货状态**/
    private SendStatusEnum sendStatus;
    /**发货时间**/
    private Date sendTime;
    /**对账状态**/
    private PayStatusEnum payStatus;
    /**总金额**/
    private BigDecimal totalMoney;
    /**合同编号**/
    private String contractCode;
    /**订货单号**/
    private String orderCode;
    /**对账时间**/
    private Date payTime;
    /**操作人**/
    private String operator;
    /**操作人姓名**/
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
