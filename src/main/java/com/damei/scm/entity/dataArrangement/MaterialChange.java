package com.damei.scm.entity.dataArrangement;

import com.damei.scm.common.IdEntity;

import java.util.Date;

public class MaterialChange  extends IdEntity {

    private Long contractId;
    private Date changeDate;
    private String changeOrderNumber;
    private String describation;
    private String createAccount;
    private Date createDate;

    private String delAccount;
    private Date delDate;
    private Integer delStatus;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getChangeOrderNumber() {
        return changeOrderNumber;
    }

    public void setChangeOrderNumber(String changeOrderNumber) {
        this.changeOrderNumber = changeOrderNumber == null ? null : changeOrderNumber.trim();
    }
    public String getDescribation() {
        return describation;
    }

    public void setDescribation(String describation) {
        this.describation = describation == null ? null : describation.trim();
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