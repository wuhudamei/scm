package com.mdni.scm.rest.dataArrangement;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.dataArrangement.MaterialBaseMain;
import com.mdni.scm.entity.eum.dataArrangement.MetarialTypeEnum;
import com.mdni.scm.service.dataArrangement.MaterialBaseMainService;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 基装主材service类
 * Created by 李万财 on 2017-08-04.
 */
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
			baseMain.setDelStatus(0);//删除的状态，默认为 0 ，没删除
			service.insert(baseMain);
		}
		return StatusDto.buildDataSuccessStatusDto(baseMain.getId());
	}

	/**
	 *根据合同id以及材料类别查询对应的材料列表
	 * @param contractId
	 * @param metarialType
	 * @return
	 */
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	public Object findByContractIdAndMetarialType(
			@RequestParam(required = true) Long contractId,
			@RequestParam(required = true) MetarialTypeEnum metarialType){
		return StatusDto.buildDataSuccessStatusDto(this.service.findByContractIdAndMetarialType(contractId,metarialType));
	}

	/**
	 *
	 * @param contractId
	 * @param metarialType
	 * @return
	 */
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
