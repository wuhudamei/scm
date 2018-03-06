package cn.damei.scm.repository.customer;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.entity.customer.CustomerContract;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface CustomerContractDao extends CrudDao<CustomerContract> {

	CustomerContract getByCode(@Param("code") String code);
	CustomerContract getById(@Param("id")Long contractId,@Param("code") String code);

    String getContractCodeById(Long contractId);
}
