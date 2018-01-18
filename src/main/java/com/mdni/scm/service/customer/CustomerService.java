package com.mdni.scm.service.customer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.customer.Customer;
import com.mdni.scm.repository.customer.CustomerDao;

/**
 * 
 * 大诚若谷信息技术有限公司 功能：客户Service
 * 
 * @@author张俊奎 时间：2017年6月22日下午2:15:43
 */
@Service
public class CustomerService extends CrudService<CustomerDao, Customer> {

	@Autowired
	private CustomerDao customerDao;

	public boolean codeIsExist(String code,Long id) {
		if (StringUtils.isBlank(code)) {
			return false;
		}
		Customer byCode = this.customerDao.getByCode(code);
		if (byCode != null) {
			if (byCode.getId() == id) {
				return false;
			}
			return true;

		}
		return false;
	}

}
