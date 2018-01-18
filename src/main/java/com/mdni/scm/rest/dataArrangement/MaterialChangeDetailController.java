package com.mdni.scm.rest.dataArrangement;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.dataArrangement.MaterialChange;
import com.mdni.scm.entity.dataArrangement.MaterialChangeDetail;
import com.mdni.scm.service.dataArrangement.MaterialChangeDetailService;
import com.mdni.scm.service.dataArrangement.MaterialChangeService;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你scm  变更详情 Controller</dd>
 * <dd>@date：2017/8/5  10:30</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@RestController
@SuppressWarnings("all")
@RequestMapping("/api/material/changeDetail")
public class MaterialChangeDetailController extends BaseComController<MaterialChangeDetailService, MaterialChangeDetail> {
	private final Integer delStatus = 1;
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public Object save(@RequestBody MaterialChangeDetail materialChange) {
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
     *通过 chang id 查询列表
	 * @param contractId
     * @return
     */
	@RequestMapping(value = "findByChangeId", method = RequestMethod.GET)
	public Object findByChangeId(
			@RequestParam(required = true) Long changeId){
		return StatusDto.buildDataSuccessStatusDto(this.service.findByChangeId(changeId));
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
