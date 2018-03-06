package cn.damei.scm.service.dataArrangement;

import cn.damei.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import cn.damei.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.repository.dataArrangement.MaterialCustomerContractConfirmDao;
import org.springframework.stereotype.Service;


@Service
public class MaterialCustomerContractConfirmService extends CrudService<MaterialCustomerContractConfirmDao,MaterialCustomerContractConfirm> {

    public MaterialCustomerContractConfirm getByoriginalId(Long originalId,MaterialCustomerContractDataTypeEnum dataType){
        return this.entityDao.getByOriginalIdAndDataType(originalId,dataType);
    }
}
