package com.damei.scm.common.dto;

import com.damei.scm.common.utils.ExcelTitle;
import org.apache.commons.lang3.StringUtils;

//导入的商品dto
public class ProductForExcuteDto {


    @ExcelTitle(title = "商品名称", order = 1)
    private String name;

    @ExcelTitle(title = "区域供应商", order = 2)
    private String regionName;

    @ExcelTitle(title = "商品供货商", order = 3)
    private String supplierName;

    @ExcelTitle(title = "商品分类", order = 4)
    private String catalogName;

    @ExcelTitle(title = "商品品牌", order = 5)
    private String brandName;

    @ExcelTitle(title = "计量单位", order = 6)
    private String measureUnitName;

    @ExcelTitle(title = "规格", order = 7)
    private String spec;

    @ExcelTitle(title = "型号", order = 8)
    private String model;


    @ExcelTitle(title = "销售属性名1", order = 9)
    private String attribute1Name;

    @ExcelTitle(title = "销售属性名2", order = 10)
    private String attribute2Name;

    @ExcelTitle(title = "销售属性名3", order = 11)
    private String attribute3Name;

    @ExcelTitle(title = "属性值1", order = 12)
    private String attribute1;

    @ExcelTitle(title = "属性值2", order = 13)
    private String attribute2;

    @ExcelTitle(title = "属性值3", order = 14)
    private String attribute3;

    @ExcelTitle(title = "库存数", order = 15)
    private Integer stock;

    @ExcelTitle(title = "网真采购价", order = 16)
    private String strSupplyPrice;
    @ExcelTitle(title = "门店采购价", order = 17)
    private String strStorePrice;
    @ExcelTitle(title = "门店销售价", order = 18)
    private String strSalePrice;
    @ExcelTitle(title = "升级项价", order = 19)
    private String strUpgradePrice;
    @ExcelTitle(title = "增项价", order = 20)
    private String strAddPrice;
    @ExcelTitle(title = "减项价", order = 21)
    private String strReducePrice;

    private Double reducePrice;
    private Double storePrice;
    private Double supplyPrice;
    private Double salePrice;
    private Double upgradePrice;
    private Double addPrice;
    private String repeatFlag;

    public String getStrSupplyPrice() {
        return strSupplyPrice;
    }

    public void setStrSupplyPrice(String strSupplyPrice) {
        this.strSupplyPrice = strSupplyPrice;
    }

    public String getStrStorePrice() {
        return strStorePrice;
    }

    public void setStrStorePrice(String strStorePrice) {
        this.strStorePrice = strStorePrice;
    }

    public String getStrSalePrice() {
        return strSalePrice;
    }

    public void setStrSalePrice(String strSalePrice) {
        this.strSalePrice = strSalePrice;
    }

    public String getStrUpgradePrice() {
        return strUpgradePrice;
    }

    public void setStrUpgradePrice(String strUpgradePrice) {
        this.strUpgradePrice = strUpgradePrice;
    }

    public String getStrAddPrice() {
        return strAddPrice;
    }

    public void setStrAddPrice(String strAddPrice) {
        this.strAddPrice = strAddPrice;
    }

    public String getStrReducePrice() {
        return strReducePrice;
    }

    public void setStrReducePrice(String strReducePrice) {
        this.strReducePrice = strReducePrice;
    }

    public String getRepeatFlag() {
        return repeatFlag;
    }

    public void setRepeatFlag(String repeatFlag) {
        this.repeatFlag = repeatFlag;
    }

    public Double getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(Double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public Double getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(Double storePrice) {
        this.storePrice = storePrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getUpgradePrice() {
        return upgradePrice;
    }

    public void setUpgradePrice(Double upgradePrice) {
        this.upgradePrice = upgradePrice;
    }

    public Double getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(Double addPrice) {
        this.addPrice = addPrice;
    }

    public Double getReducePrice() {
        return reducePrice;
    }

    public void setReducePrice(Double reducePrice) {
        this.reducePrice = reducePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMeasureUnitName() {
        return measureUnitName;
    }

    public void setMeasureUnitName(String measureUnitName) {
        this.measureUnitName = measureUnitName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getAttribute1Name() {
        return StringUtils.trimToNull(attribute1Name);
    }

    public void setAttribute1Name(String attribute1Name) {
        this.attribute1Name = attribute1Name;
    }

    public String getAttribute2Name() {
        return StringUtils.trimToNull(attribute2Name);
    }

    public void setAttribute2Name(String attribute2Name) {
        this.attribute2Name = attribute2Name;
    }

    public String getAttribute3Name() {
        return StringUtils.trimToNull(attribute3Name);
    }

    public void setAttribute3Name(String attribute3Name) {
        this.attribute3Name = attribute3Name;
    }

    public String getAttribute1() {
        return StringUtils.trimToNull(attribute1);
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return StringUtils.trimToNull(attribute2);
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return StringUtils.trimToNull(attribute3);
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}