package cn.damei.scm.repository.customer;

import cn.damei.scm.entity.customer.Customer;
import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;

@MyBatisRepository
public interface CustomerDao extends CrudDao<Customer> {
	public Customer getByCode(@Param("code") String code);

}
