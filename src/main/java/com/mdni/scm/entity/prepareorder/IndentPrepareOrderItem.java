package com.mdni.scm.entity.prepareorder;

import com.mdni.scm.common.IdEntity;

import java.math.BigDecimal;

/**
 * @Description: 订货预备单sku
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 18:45.
 */
public class IndentPrepareOrderItem extends IdEntity {

    //预备单id
    private Long prepareOrderId;
    //skuid
    private Long skuId;
    //sku名称
    private String skuName;
    //型号
    private String model;
    //规格
    private String spec;
    //属性值1
    private String attribute1;
    //属性值2
    private String attribute2;
    //属性值3
    private String attribute3;
    //供货商的供货价
    private BigDecimal supplyPrice;
    //订货数量
    private BigDecimal quantity;
    //安装位置
    private String installationLocation;
    //商品单位
    private String specUnit;
    //片数
    private Long tabletNum;

    //以下非数据库字段
    //供应商Id
    private Long supplierId;
    //skuCode
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
