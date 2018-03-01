package com.damei.scm.repository.customer;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.customer.CustomerContract;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface CustomerContractDao extends  CrudDao<CustomerContract>{

	CustomerContract getByCode(@Param("code") String code);
	CustomerContract getById(@Param("id")Long contractId,@Param("code") String code);

    String getContractCodeById(Long contractId);
}
