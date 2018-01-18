package com.mdni.scm.rest.customer;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.customer.Customer;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.prod.Store;
import com.mdni.scm.service.customer.CustomerService;
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

/**
 * 
 * 大诚若谷信息技术有限公司 功能：客户Controller 作者:张俊奎 时间：2017年6月22日下午2:16:52
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerRestController extends BaseComController<CustomerService, Customer> {

	@Autowired
	private CustomerService customerService;

	// 查询
	@RequestMapping("/list")
	@Override
	public Object search(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "id") String orderColumn,
			@RequestParam(defaultValue = "DESC") String orderSort) {

		ShiroUser loginUser = WebUtils.getLoggedUser();
		AccoutTypeEnum acctType1 = loginUser.getAcctType();
		if(acctType1==null){
			return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
		}

		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
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
	public Object save(Customer customer) {
		if (!AccoutTypeEnum.FINANCE.equals(WebUtils.getLoggedUser().getAcctType())) {
			return StatusDto.buildFailureStatusDto("只有财务人员才可以编辑或创建客户");
		}
		// 判断客户编码是否存在,true = 已经存在, false = 不存在
		/*if (this.customerService.codeIsExist(customer.getCode(), customer.getId())) {
			return StatusDto.buildFailureStatusDto("客户编码不合法");
		}*/
		if (customer.getId() != null) {
			service.update(customer);
		} else {
			customer.setStore(new Store(WebUtils.getLoggedUser().getStoreCode()));
			service.insert(customer);
		}
		return StatusDto.buildSuccessStatusDto("操作成功");

	}

}
