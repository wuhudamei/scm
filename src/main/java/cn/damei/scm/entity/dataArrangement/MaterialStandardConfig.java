package cn.damei.scm.entity.dataArrangement;

import cn.damei.scm.common.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.damei.scm.common.utils.DateUtil;

import java.util.Date;

public class MaterialStandardConfig extends IdEntity {


    private Long contractId;


    private String standardProjectName;


    private String location;


    private String brandMeal;


    private String model;


    private String spec;


    private String unit;


    private Double quantity;


    private Double wastageQuantity;


    private Double actualQuantity;


    private String remark;


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