package com.mdni.scm.rest.customer;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.customer.Customer;
import com.mdni.scm.entity.customer.CustomerContract;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.entity.prod.Store;
import com.mdni.scm.service.customer.CustomerContractService;
import com.mdni.scm.service.customer.CustomerService;
import com.mdni.scm.service.operatorLog.OperateLogService;
import com.mdni.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/contract")
public class CustomerContractRestController extends BaseComController<CustomerContractService, CustomerContract> {

	@Autowired
	private CustomerContractService customerContractService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OperateLogService operateLogService;

	// 查询
	@RequestMapping("/list")
	public Object search(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "id") String orderColumn,
			@RequestParam(defaultValue = "DESC") String orderSort, @RequestParam(required = false) Long customerId) {

		ShiroUser loginUser = WebUtils.getLoggedUser();
		AccoutTypeEnum acctType1 = loginUser.getAcctType();
		if(acctType1==null){
			return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (customerId != null && customerId > 0) {
			params.put("customerId", customerId);
		}
		if( StringUtils.isNotBlank( WebUtils.getLoggedUser().getStoreCode() ) ){
			params.put("storeCode", WebUtils.getLoggedUser().getStoreCode());
		}
		params.put(Constants.PAGE_OFFSET, offset);
		params.put(Constants.PAGE_SIZE, limit);
		params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public Object save(CustomerContract customerContract) {
		ShiroUser user = WebUtils.getLoggedUser();
		String name = user.getName();
		OperateLog operateLog = new OperateLog();
		// 判断客户编码是否存在,true = 已经存在, false = 不存在
		if (this.customerContractService.codeIsExist(customerContract.getContractCode(), customerContract.getId())) {
			return StatusDto.buildFailureStatusDto("合同编码已存在");
		}
		if (customerContract.getId() != null) {
			service.update(customerContract);
		} else {
			if (customerContract.getCustomer().getId() == null) {

				if (!AccoutTypeEnum.FINANCE.equals(WebUtils.getLoggedUser().getAcctType())) {
					return StatusDto.buildFailureStatusDto("只有财务人员才可以编辑或创建客户");
				}
				//添加门店
				Customer customer = customerContract.getCustomer();
				Store store = new Store();
				store.setCode(user.getStoreCode());
				customer.setStore(store);
				this.customerService.insert(customer);
			}
			service.insert(customerContract);
		}
		return StatusDto.buildSuccessStatusDto("操作成功");

	}
	@RequestMapping("/getById")
	public Object getById(@RequestParam(required = false) Long contractId,@RequestParam(required = false)String code){
		CustomerContract customerContract = this.customerContractService.getById(contractId,code);
		return StatusDto.buildDataSuccessStatusDto(customerContract);
	}

}
