package com.mdni.scm.repository.customer;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.customer.CustomerContract;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface CustomerContractDao extends  CrudDao<CustomerContract>{

	CustomerContract getByCode(@Param("code") String code);
	CustomerContract getById(@Param("id")Long contractId,@Param("code") String code);

    String getContractCodeById(Long contractId);
}
