package cn.damei.scm.entity.dataArrangement;

import cn.damei.scm.common.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cn.damei.scm.common.utils.DateUtil;
import cn.damei.scm.entity.eum.dataArrangement.MaterialContractOperateEnum;
import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialContractOperateTurnover extends IdEntity {

    private Long contractId;
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date operateTime;

    private String operateAccount;

    private MaterialCustomerContract materialCustomerContract;

    private MaterialContractOperateEnum operateType;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateAccount() {
        return operateAccount;
    }

    public void setOperateAccount(String operateAccount) {
        this.operateAccount = operateAccount;
    }

    public MaterialContractOperateEnum getOperateType() {
        return operateType;
    }

    public void setOperateType(MaterialContractOperateEnum operateType) {
        this.operateType = operateType;
    }

    public MaterialCustomerContract getMaterialCustomerContract() {
        return materialCustomerContract;
    }

    public void setMaterialCustomerContract(MaterialCustomerContract materialCustomerContract) {
        this.materialCustomerContract = materialCustomerContract;
    }
}