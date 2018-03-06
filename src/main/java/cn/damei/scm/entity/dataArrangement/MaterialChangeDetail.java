package cn.damei.scm.entity.dataArrangement;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.entity.eum.dataArrangement.MaterialChangeDetailChangeTypeEnum;
import cn.damei.scm.entity.eum.dataArrangement.MaterialChangeDetailTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.damei.scm.common.utils.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class MaterialChangeDetail extends IdEntity {

    private MaterialChangeDetailChangeTypeEnum materialType;

    private String projectName;

    private String location;

    private String brand;

    private BigDecimal amount;

    private String unit;

    private String specification;

    private String model;

    private BigDecimal price;

    private BigDecimal total;

    private String description;

    private MaterialChangeDetailTypeEnum changeType;

    private Long changeId;

    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createDate;

    private String createAccount;

    private BigDecimal wastageCost;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private Double holeHigh;
    private Double holeWide;
    private Double holeThuck;
    private String addStack;

    private String delAccount;
    private Date delDate;
    private Integer delStatus;

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