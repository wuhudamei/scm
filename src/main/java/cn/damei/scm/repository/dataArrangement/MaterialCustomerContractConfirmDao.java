package cn.damei.scm.repository.dataArrangement;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import cn.damei.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface MaterialCustomerContractConfirmDao extends CrudDao<MaterialCustomerContractConfirm> {


    MaterialCustomerContractConfirm getByOriginalIdAndDataType(@Param("originalId") Long originalId, @Param("dataType") MaterialCustomerContractDataTypeEnum dataType);
}