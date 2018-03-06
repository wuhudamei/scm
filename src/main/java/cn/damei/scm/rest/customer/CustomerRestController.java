package cn.damei.scm.rest.customer;

import cn.damei.scm.entity.customer.Customer;
import cn.damei.scm.service.customer.CustomerService;
import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.Constants;
import cn.damei.scm.common.dto.BootstrapPage;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.entity.eum.AccoutTypeEnum;
import cn.damei.scm.entity.prod.Store;
import cn.damei.scm.shiro.ShiroUser;
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
@RequestMapping("/api/customer")
public class CustomerRestController extends BaseComController<CustomerService, Customer> {

	@Autowired
	private CustomerService customerService;

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
		if (customer.getId() != null) {
			service.update(customer);
		} else {
			customer.setStore(new Store(WebUtils.getLoggedUser().getStoreCode()));
			service.insert(customer);
		}
		return StatusDto.buildSuccessStatusDto("操作成功");

	}

}
