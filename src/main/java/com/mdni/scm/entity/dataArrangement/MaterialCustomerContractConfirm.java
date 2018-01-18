package com.mdni.scm.entity.dataArrangement;

import com.mdni.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;

/**
 * 合同数据核查补充信息
 * Created by MrLi on 2017-08-15.
 */
public class MaterialCustomerContractConfirm  extends MaterialCustomerContract{
    //原始数据id
    private Long originalId;

    //录入类型（input，check）
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
