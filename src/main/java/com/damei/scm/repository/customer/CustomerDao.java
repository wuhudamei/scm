package com.damei.scm.repository.customer;

import org.apache.ibatis.annotations.Param;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.customer.Customer;

@MyBatisRepository
public interface CustomerDao extends CrudDao<Customer> {
	public Customer getByCode(@Param("code") String code);

}
