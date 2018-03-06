package cn.damei.scm.entity.dataArrangement;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.entity.eum.dataArrangement.MetarialTypeEnum;
import cn.damei.scm.entity.eum.dataArrangement.FeeTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

public class MaterialBaseMain extends IdEntity {
    private Long contractId;
    private String projectName;
    private MetarialTypeEnum metarialType;
    private FeeTypeEnum feeType;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal wastage;
    private BigDecimal price;
    private BigDecimal mainMetarialPrice;
    private BigDecimal accessoriesMetarialPrice;
    private BigDecimal artificialFee;
    private BigDecimal feeTotal;
    private String remarks;
    private String createAccount;
    private Date createDate;
    private String delAccount;
    private Date delDate;
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