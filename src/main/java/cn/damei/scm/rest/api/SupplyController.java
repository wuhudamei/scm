package cn.damei.scm.rest.api;


import cn.damei.scm.common.BaseComController;
import cn.damei.scm.entity.supply.Supply;
import cn.damei.scm.service.supply.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/externalApi/supply")
public class SupplyController extends BaseComController<SupplyService, Supply> {

	@Autowired
	private SupplyService supplyService;
	@RequestMapping(value = "/findSupplyInfoByContractNo", method = RequestMethod.POST)
	public Object list(@RequestParam(required = false) String contractNo){
		return this.supplyService.findSupplyInfoByContractNo(contractNo);
	}

}