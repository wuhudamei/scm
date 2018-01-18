package com.mdni.scm.rest.api;


import com.mdni.scm.common.BaseComController;
import com.mdni.scm.entity.supply.Supply;
import com.mdni.scm.service.supply.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 
 * <dl>
 * <dd>描述: 供应商</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月15日 下午1:13:51</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/externalApi/supply")
public class SupplyController extends BaseComController<SupplyService, Supply> {

	@Autowired
	private SupplyService supplyService;
	/**
	 * 查询列表
	 */
	@RequestMapping(value = "/findSupplyInfoByContractNo", method = RequestMethod.POST)
	public Object list(@RequestParam(required = false) String contractNo){
		return this.supplyService.findSupplyInfoByContractNo(contractNo);
	}

}