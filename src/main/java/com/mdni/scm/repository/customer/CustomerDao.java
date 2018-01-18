package com.mdni.scm.repository.customer;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.customer.Customer;

/**
 * 
 * 大诚若谷信息技术有限公司 功能：客户DAO 作者:张俊奎 时间：2017年6月22日下午2:16:26
 */
@MyBatisRepository
public interface CustomerDao extends CrudDao<Customer> {
	public Customer getByCode(@Param("code") String code);

}
