package cn.damei.scm.rest.dataArrangement;

import com.google.common.collect.Maps;
import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.MapUtils;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.entity.dataArrangement.MaterialBaseMain;
import cn.damei.scm.entity.eum.dataArrangement.MetarialTypeEnum;
import cn.damei.scm.service.dataArrangement.MaterialBaseMainService;
import cn.damei.scm.shiro.ShiroUser;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/material/baseMain")
public class MaterialBaseMainRestController extends BaseComController<MaterialBaseMainService, MaterialBaseMain> {
	private final Integer delStatus = 1;
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public Object save(@RequestBody MaterialBaseMain baseMain) {

		if (baseMain.getId() != null) {
			service.update(baseMain);
		} else {
			baseMain.setCreateAccount(WebUtils.getLoggedUser().getLoginName());
			baseMain.setCreateDate(new Date());
			service.insert(baseMain);
		}
		return StatusDto.buildDataSuccessStatusDto(baseMain.getId());
	}

	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	public Object findByContractIdAndMetarialType(
			@RequestParam(required = true) Long contractId,
			@RequestParam(required = true) MetarialTypeEnum metarialType){
		return StatusDto.buildDataSuccessStatusDto(this.service.findByContractIdAndMetarialType(contractId,metarialType));
	}
	@RequestMapping(value = "feeSummaryForType", method = RequestMethod.GET)
	public Object feeSummaryForType(
			@RequestParam(required = true) Long contractId,
			@RequestParam(required = true) MetarialTypeEnum metarialType){
		return StatusDto.buildDataSuccessStatusDto(this.service.findForTotal(contractId,metarialType));
	}

	@RequestMapping("/delete")
	public Object delete(@RequestParam(required = false) Long id){
		if (id == null) {
			return StatusDto.buildFailureStatusDto("记录不存在");
		}
		ShiroUser loggedUser = WebUtils.getLoggedUser();
		String delAccount = loggedUser.getName();
		Date delDate = new Date();
		Map<String, Object> paramMap = Maps.newHashMap();
		MapUtils.putNotNull(paramMap,"delAccount",delAccount);
		MapUtils.putNotNull(paramMap,"delDate",delDate);
		MapUtils.putNotNull(paramMap,"delStatus",delStatus);
		MapUtils.putNotNull(paramMap,"id",id);
		this.service.delete(paramMap);
		return StatusDto.buildDataSuccessStatusDto("删除成功！");
	}
}
