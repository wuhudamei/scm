package com.mdni.scm.rest.dataArrangement;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.dataArrangement.MaterialChange;
import com.mdni.scm.service.dataArrangement.MaterialChangeService;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 选材service类
 * Created by 李万财 on 2017-08-04.
 */
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
			materialChange.setDelStatus(0);//删除的状态，默认为 0 ，没删除
			service.insert(materialChange);
		}
		return StatusDto.buildDataSuccessStatusDto(materialChange.getId());
	}

    /**
     *根据合同id对应的材料列表
	 * @param contractId
     * @return
     */
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
