package cn.damei.scm.entity.prepareorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import cn.damei.scm.common.IdEntity;
import cn.damei.scm.common.utils.DateUtil;

import java.util.Date;
import java.util.List;

public class IndentPrepareOrder extends IdEntity {


    private String contractCode;

    private String dataSource;

    private String status;

    private Long brandId;

    private String brandName;

    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date createTime;

    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date updateTime;

    private String updateAccount;

    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date switchTime;

    private String customerName;

    private String installationLocation;

    private List<IndentPrepareOrderItem> indentPrepareOrderItemList;

    private Long contractId;

    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateAccount() {
        return updateAccount;
    }
    public void setUpdateAccount(String updateAccount) {
        this.updateAccount = updateAccount;
    }
    public Date getSwitchTime() {
        return switchTime;
    }
    public void setSwitchTime(Date switchTime) {
        this.switchTime = switchTime;
    }
    public Long getBrandId() {
        return brandId;
    }
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getInstallationLocation() {
        return installationLocation;
    }
    public void setInstallationLocation(String installationLocation) {
        this.installationLocation = installationLocation;
    }
    public List<IndentPrepareOrderItem> getIndentPrepareOrderItemList() {
        return indentPrepareOrderItemList;
    }
    public void setIndentPrepareOrderItemList(List<IndentPrepareOrderItem> indentPrepareOrderItemList) {
        this.indentPrepareOrderItemList = indentPrepareOrderItemList;
    }
    public Long getContractId() {
        return contractId;
    }
    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
}
