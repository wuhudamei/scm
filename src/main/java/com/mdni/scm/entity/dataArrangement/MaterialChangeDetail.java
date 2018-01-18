package com.mdni.scm.entity.dataArrangement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.entity.eum.dataArrangement.MaterialChangeDetailChangeTypeEnum;
import com.mdni.scm.entity.eum.dataArrangement.MaterialChangeDetailTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <dl>
 * <dd>Description: 美得你scm  变更详情 实体</dd>
 * <dd>@date：2017/8/5  10:30</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
public class MaterialChangeDetail extends IdEntity {

    /**
     * 变更类型  主材变更    基装变更
     */
    private MaterialChangeDetailChangeTypeEnum materialType;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 位置
     */
    private String location;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String specification;

    /**
     * 型号
     */
    private String model;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 合计
     */
    private BigDecimal total;

    /**
     * 工艺说明及材料说明
     */
    private String description;

    /**
     * 类型  增项   减项
     */
    private MaterialChangeDetailTypeEnum changeType;

    /**
     * 变更id
     */
    private Long changeId;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createDate;

    /**
     * 创建人
     */
    private String createAccount;

    //损耗
    private BigDecimal wastageCost;
    //材料费
    private BigDecimal materialCost;
    //人工费
    private BigDecimal laborCost;
    //洞口尺寸_高
    private Double holeHigh;
    //洞口尺寸_宽
    private Double holeWide;
    //洞口尺寸_厚
    private Double holeThuck;
    //加垛
    private String addStack;

    private String delAccount;//删除人
    private Date delDate;//删除时间
    private Integer delStatus;//删除状态

    private static final long serialVersionUID = 1L;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getChangeId() {
        return changeId;
    }

    public void setChangeId(Long changeId) {
        this.changeId = changeId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public BigDecimal getWastageCost() {
        return wastageCost;
    }

    public void setWastageCost(BigDecimal wastageCost) {
        this.wastageCost = wastageCost;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public Double getHoleHigh() {
        return holeHigh;
    }

    public void setHoleHigh(Double holeHigh) {
        this.holeHigh = holeHigh;
    }

    public Double getHoleWide() {
        return holeWide;
    }

    public void setHoleWide(Double holeWide) {
        this.holeWide = holeWide;
    }

    public Double getHoleThuck() {
        return holeThuck;
    }

    public void setHoleThuck(Double holeThuck) {
        this.holeThuck = holeThuck;
    }

    public String getAddStack() {
        return addStack;
    }

    public void setAddStack(String addStack) {
        this.addStack = addStack;
    }

    public MaterialChangeDetailChangeTypeEnum getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialChangeDetailChangeTypeEnum materialType) {
        this.materialType = materialType;
    }

    public MaterialChangeDetailTypeEnum getChangeType() {
        return changeType;
    }

    public void setChangeType(MaterialChangeDetailTypeEnum changeType) {
        this.changeType = changeType;
    }

    public String getDelAccount() {
        return delAccount;
    }

    public void setDelAccount(String delAccount) {
        this.delAccount = delAccount;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }
}