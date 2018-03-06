package cn.damei.scm.service.customer;

import cn.damei.scm.repository.customer.CustomerContractDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.customer.CustomerContract;

@Service
public class CustomerContractService extends CrudService<CustomerContractDao, CustomerContract> {

	@Autowired
	private CustomerContractDao customerContractDao;

	public boolean codeIsExist(String code, Long id) {
		if (StringUtils.isBlank(code)) {
			return false;
		}
		CustomerContract byCode = this.customerContractDao.getByCode(code);
		if (byCode != null) {
			if (byCode.getId().equals(id)) {
				return false;
			}
			return true;
		}
		return false;
	}
	public CustomerContract getById(Long contractId,String code){
		return this.customerContractDao.getById(contractId,code);
	}

    public String getContractCodeById(Long contractId) {
		String contractCode = customerContractDao.getContractCodeById(contractId);
		return contractCode;
	}
}
