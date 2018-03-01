package com.damei.scm.rest.dataArrangement;

import com.google.common.collect.Maps;
import com.damei.scm.common.BaseComController;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.utils.MapUtils;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.dataArrangement.MaterialChange;
import com.damei.scm.service.dataArrangement.MaterialChangeService;
import com.damei.scm.shiro.ShiroUser;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/material/change")
public class MaterialChangeRestController extends BaseComController<MaterialChangeService, MaterialChange> {
	private final Integer delStatus = 1;
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public Object save(@RequestBody MaterialChange materialChange) {

		if (materialChange.getId() != null) {
			service.update(materialChange);
		} else {
			materialChange.setCreateAccount(WebUtils.getLoggedUser().getLoginName());
			materialChange.setCreateDate(new Date());
			service.insert(materialChange);
		}
		return StatusDto.buildDataSuccessStatusDto(materialChange.getId());
	}

	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	public Object findByContractId(
			@RequestParam(required = true) Long contractId){
		return StatusDto.buildDataSuccessStatusDto(this.service.findByContractId(contractId));
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
		return StatusDto.buildDataSuccessStatusDto("删除成功");
	}

}
