package com.mdni.scm.entity.dataArrangement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;

import java.util.Date;

/**
 * <dl>
 * <dd>Description: 美得你scm 房屋拆改实体</dd>
 * <dd>@date：2017/8/4  18:06</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
public class MaterialHouseReform extends IdEntity {

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 项目名称
     */
    private String refomProjectName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private Double quantity;

    /**
     * 损耗数量
     */
    private Double wastageQuantity;

    /**
     * 材料费（主材单价）
     */
    private Double materialMasterFee;

    /**
     * 材料费（辅材单价）
     */
    private Double matrialAssistFee;

    /**
     * 人工费
     */
    private Double manMadeFee;

    /**
     * 单价
     */
    private Double price;

    /**
     * 合计
     */
    private Double amount;

    /**
     * 工艺说明及材料说明
     */
    private String technologyMaterialExplain;

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