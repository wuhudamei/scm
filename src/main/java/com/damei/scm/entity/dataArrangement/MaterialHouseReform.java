package com.damei.scm.entity.dataArrangement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.damei.scm.common.IdEntity;
import com.damei.scm.common.utils.DateUtil;

import java.util.Date;

public class MaterialHouseReform extends IdEntity {

    private Long contractId;

    private String refomProjectName;

    private String unit;

    private Double quantity;

    private Double wastageQuantity;

    private Double materialMasterFee;

    private Double matrialAssistFee;

    private Double manMadeFee;

    private Double price;

    private Double amount;

    private String technologyMaterialExplain;

    private String createAccount;

    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createDate;

    private String delAccount;
    private Date delDate;
    private Integer delStatus;

    private static final long serialVersionUID = 1L;


    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getRefomProjectName() {
        return refomProjectName;
    }

    public void setRefomProjectName(String refomProjectName) {
        this.refomProjectName = refomProjectName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getWastageQuantity() {
        return wastageQuantity;
    }

    public void setWastageQuantity(Double wastageQuantity) {
        this.wastageQuantity = wastageQuantity;
    }

    public Double getMaterialMasterFee() {
        return materialMasterFee;
    }

    public void setMaterialMasterFee(Double materialMasterFee) {
        this.materialMasterFee = materialMasterFee;
    }

    public Double getMatrialAssistFee() {
        return matrialAssistFee;
    }

    public void setMatrialAssistFee(Double matrialAssistFee) {
        this.matrialAssistFee = matrialAssistFee;
    }

    public Double getManMadeFee() {
        return manMadeFee;
    }

    public void setManMadeFee(Double manMadeFee) {
        this.manMadeFee = manMadeFee;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTechnologyMaterialExplain() {
        return technologyMaterialExplain;
    }

    public void setTechnologyMaterialExplain(String technologyMaterialExplain) {
        this.technologyMaterialExplain = technologyMaterialExplain;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
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