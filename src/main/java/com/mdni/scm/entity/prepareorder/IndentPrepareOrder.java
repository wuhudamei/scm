package com.mdni.scm.entity.prepareorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * @Description: 订货预备单
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 18:41.
 */
public class IndentPrepareOrder extends IdEntity {

    //项目编码
    private String contractCode;
    //数据来源，（选材、变更）--枚举(Constants.PAGE_TYPE_SELECT): select/change
    private String dataSource;
    //预备单状态:待转单、已转单、已作废
    private String status;
    //品牌id
    private Long brandId;
    //品牌名称
    private String brandName;
    //创建时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date createTime;
    //更新时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date updateTime;
    //更新人
    private String updateAccount;
    //转单时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date switchTime;


    //以下非数据库字段
    //客户姓名
    private String customerName;
    //安装位置
    private String installationLocation;
    //预备单item项
    private List<IndentPrepareOrderItem> indentPrepareOrderItemList;
    //合同id
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
