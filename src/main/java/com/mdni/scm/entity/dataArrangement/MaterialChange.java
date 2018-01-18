package com.mdni.scm.entity.dataArrangement;

import com.mdni.scm.common.IdEntity;

import java.util.Date;

/**
 * 选材变更实体类
 * Created by 李万财 on 2017-08-04.
 */
public class MaterialChange  extends IdEntity {

    //合同id
    private Long contractId;
    //变更时间
    private Date changeDate;
    //变更序号
    private String changeOrderNumber;
    //变更描述
    private String describation;
    //创建人
    private String createAccount;
    //创建时间
    private Date createDate;

    private String delAccount;//删除人
    private Date delDate;//删除时间
    private Integer delStatus;//删除状态

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