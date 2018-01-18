package com.mdni.scm.service.dataArrangement;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import com.mdni.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;
import com.mdni.scm.repository.dataArrangement.MaterialCustomerContractConfirmDao;
import org.springframework.stereotype.Service;


@Service
public class MaterialCustomerContractConfirmService extends CrudService<MaterialCustomerContractConfirmDao,MaterialCustomerContractConfirm> {

    /**
     * 根据原始数据id获取合同信息
     * @param originalId
     * @param dataType
     * @return
     */
    public MaterialCustomerContractConfirm getByoriginalId(Long originalId,MaterialCustomerContractDataTypeEnum dataType){
        return this.entityDao.getByOriginalIdAndDataType(originalId,dataType);
    }
}
