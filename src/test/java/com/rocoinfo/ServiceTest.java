package com.rocoinfo;

import com.mdni.scm.common.Constants;
import com.mdni.scm.entity.account.User;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.service.order.IndentOrderService;
import com.mdni.scm.service.prod.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weiys on 16/12/17.
 */
public class ServiceTest extends SpringContextTestCase {
	@Autowired
	private ProductService productService;
	@Autowired
	private IndentOrderService massageService;

	@Test
	public void getById() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(Constants.PAGE_OFFSET, 0);
		params.put(Constants.PAGE_SIZE, 10);
		params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.DESC, "hit_count"));
	}


	@Test
	public void testInsertUser(){

		User user = new User();
		user.setId(1L);
		user.setAcctType(AccoutTypeEnum.ADMIN);
		System.out.print(user);

	}

}
