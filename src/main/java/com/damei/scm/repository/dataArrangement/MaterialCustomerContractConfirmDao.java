package com.damei.scm.repository.dataArrangement;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import com.damei.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface MaterialCustomerContractConfirmDao extends CrudDao<MaterialCustomerContractConfirm>{


    MaterialCustomerContractConfirm getByOriginalIdAndDataType(@Param("originalId") Long originalId,@Param("dataType") MaterialCustomerContractDataTypeEnum dataType);
}