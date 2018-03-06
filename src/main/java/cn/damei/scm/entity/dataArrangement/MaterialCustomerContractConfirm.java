package cn.damei.scm.entity.dataArrangement;

import cn.damei.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;

public class MaterialCustomerContractConfirm  extends MaterialCustomerContract{
    private Long originalId;

    private MaterialCustomerContractDataTypeEnum dataType;

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public MaterialCustomerContractDataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(MaterialCustomerContractDataTypeEnum dataType) {
        this.dataType = dataType;
    }
}
