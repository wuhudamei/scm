package com.damei.scm.entity.prepareorder;

import com.damei.scm.common.IdEntity;

import java.math.BigDecimal;

public class IndentPrepareOrderItem extends IdEntity {


    private Long prepareOrderId;

    private Long skuId;

    private String skuName;

    private String model;

    private String spec;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private BigDecimal supplyPrice;

    private BigDecimal quantity;

    private String installationLocation;

    private String specUnit;

    private Long tabletNum;

    private Long supplierId;

    private String skuCode;


    public Long getPrepareOrderId() {
        return prepareOrderId;
    }
    public void setPrepareOrderId(Long prepareOrderId) {
        this.prepareOrderId = prepareOrderId;
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
    public String getInstallationLocation() {
        return installationLocation;
    }
    public void setInstallationLocation(String installationLocation) {
        this.installationLocation = installationLocation;
    }
    public String getSpecUnit() {
        return specUnit;
    }
    public void setSpecUnit(String specUnit) {
        this.specUnit = specUnit;
    }
    public Long getTabletNum() {
        return tabletNum;
    }
    public void setTabletNum(Long tabletNum) {
        this.tabletNum = tabletNum;
    }
    public Long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    public String getSkuCode() {
        return skuCode;
    }
    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
}
