package com.mdni.scm.entity.dataArrangement;

import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.eum.dataArrangement.FeeTypeEnum;
import com.mdni.scm.entity.eum.dataArrangement.MetarialTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 基装主材实体类
 * Created by 李万财 on 2017-08-04.
 */
public class MaterialBaseMain extends IdEntity {
    //合同id
    private Long contractId;
    //项目名称
    private String projectName;
    //材料类型（基装，主材）
    private MetarialTypeEnum metarialType;
    //费用类型（增项，减项，基装综合）
    private FeeTypeEnum feeType;
    //单位
    private String unit;
    //数量
    private BigDecimal quantity;
    //损耗
    private BigDecimal wastage;
    //单价
    private BigDecimal price;
    //主材单价
    private BigDecimal mainMetarialPrice;
    //辅材单价
    private BigDecimal accessoriesMetarialPrice;
    //人工费
    private BigDecimal artificialFee;
    //费用总和
    private BigDecimal feeTotal;
    //说明
    private String remarks;
    //创建人账号
    private String createAccount;
    //创建时间
    private Date createDate;
    //删除人
    private String delAccount;
    //删除时间
    private Date delDate;
    //删除状态
    private Integer delStatus;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public MetarialTypeEnum getMetarialType() {
        return metarialType;
    }

    public void setMetarialType(MetarialTypeEnum metarialType) {
        this.metarialType = metarialType;
    }

    public FeeTypeEnum getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeTypeEnum feeType) {
        this.feeType = feeType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getWastage() {
        return wastage;
    }

    public void setWastage(BigDecimal wastage) {
        this.wastage = wastage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMainMetarialPrice() {
        return mainMetarialPrice;
    }

    public void setMainMetarialPrice(BigDecimal mainMetarialPrice) {
        this.mainMetarialPrice = mainMetarialPrice;
    }

    public BigDecimal getAccessoriesMetarialPrice() {
        return accessoriesMetarialPrice;
    }

    public void setAccessoriesMetarialPrice(BigDecimal accessoriesMetarialPrice) {
        this.accessoriesMetarialPrice = accessoriesMetarialPrice;
    }

    public BigDecimal getArtificialFee() {
        return artificialFee;
    }

    public void setArtificialFee(BigDecimal artificialFee) {
        this.artificialFee = artificialFee;
    }

    public BigDecimal getFeeTotal() {
        return feeTotal;
    }

    public void setFeeTotal(BigDecimal feeTotal) {
        this.feeTotal = feeTotal;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount == null ? null : createAccount.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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