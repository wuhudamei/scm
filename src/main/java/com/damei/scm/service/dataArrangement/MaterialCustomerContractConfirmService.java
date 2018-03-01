package com.damei.scm.service.dataArrangement;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import com.damei.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;
import com.damei.scm.repository.dataArrangement.MaterialCustomerContractConfirmDao;
import org.springframework.stereotype.Service;


@Service
public class MaterialCustomerContractConfirmService extends CrudService<MaterialCustomerContractConfirmDao,MaterialCustomerContractConfirm> {

    public MaterialCustomerContractConfirm getByoriginalId(Long originalId,MaterialCustomerContractDataTypeEnum dataType){
        return this.entityDao.getByOriginalIdAndDataType(originalId,dataType);
    }
}
