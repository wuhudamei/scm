package com.mdni.scm.repository.dataArrangement;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.dataArrangement.MaterialCustomerContract;
import com.mdni.scm.entity.eum.dataArrangement.MetarialContractStatusEnum;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@MyBatisRepository
public interface MaterialCustomerContractDao extends CrudDao<MaterialCustomerContract>{
    void updateStatus(@Param("id") Long id,
                      @Param("keyboarder") String keyboarder,
                      @Param("inputTime") Date inputTime,
                      @Param("contractStatus") MetarialContractStatusEnum contractStatus);
}