package com.mdni.scm.entity.dataArrangement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;

import java.util.Date;

/**
 * <dl>
 * <dd>Description: 美得你scm  标配录入实体</dd>
 * <dd>@date：2017/8/4  18:06</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
public class MaterialStandardConfig extends IdEntity {

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 项目名称
     */
    private String standardProjectName;

    /**
     * 位置
     */
    private String location;

    /**
     * 品牌及套餐
     */
    private String brandMeal;

    /**
     * 型号
     */
    private String model;

    /**
     * 规格
     */
    private String spec;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private Double quantity;

    /**
     * 含耗损数量
     */
    private Double wastageQuantity;

    /**
     * 实发数量
     */
    private Double actualQuantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createAccount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createDate;

    private String delAccount;//删除人
    private Date delDate;//删除时间
    private Integer delStatus;//删除状态

    private static final long serialVersionUID = 1L;


    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getStandardProjectName() {
        return standardProjectName;
    }

    public void setStandardProjectName(String standardProjectName) {
        this.standardProjectName = standardProjectName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrandMeal() {
        return brandMeal;
    }

    public void setBrandMeal(String brandMeal) {
        this.brandMeal = brandMeal;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
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

    public Double getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Double actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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