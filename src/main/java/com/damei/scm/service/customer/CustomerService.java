package com.damei.scm.service.customer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.customer.Customer;
import com.damei.scm.repository.customer.CustomerDao;

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
