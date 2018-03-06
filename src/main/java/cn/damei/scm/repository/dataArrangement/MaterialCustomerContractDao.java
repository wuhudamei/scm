package cn.damei.scm.repository.dataArrangement;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.dataArrangement.MaterialCustomerContract;
import cn.damei.scm.entity.eum.dataArrangement.MetarialContractStatusEnum;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@MyBatisRepository
public interface MaterialCustomerContractDao extends CrudDao<MaterialCustomerContract>{
    void updateStatus(@Param("id") Long id,
                      @Param("keyboarder") String keyboarder,
                      @Param("inputTime") Date inputTime,
                      @Param("contractStatus") MetarialContractStatusEnum contractStatus);
}