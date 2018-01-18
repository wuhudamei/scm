package com.mdni.scm.repository.dataArrangement;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import com.mdni.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface MaterialCustomerContractConfirmDao extends CrudDao<MaterialCustomerContractConfirm>{

    /**
     * 根据原始id查询记录
     * @param originalId
     * @return
     */
    MaterialCustomerContractConfirm getByOriginalIdAndDataType(@Param("originalId") Long originalId,@Param("dataType") MaterialCustomerContractDataTypeEnum dataType);
}