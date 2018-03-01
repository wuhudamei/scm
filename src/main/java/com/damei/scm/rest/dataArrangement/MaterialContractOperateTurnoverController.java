package com.damei.scm.rest.dataArrangement;

import com.damei.scm.common.BaseComController;
import com.damei.scm.common.Constants;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.utils.MapUtils;
import com.damei.scm.entity.dataArrangement.MaterialContractOperateTurnover;
import com.damei.scm.service.dataArrangement.MaterialContractOperateTurnoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contractOperate")
public class MaterialContractOperateTurnoverController extends BaseComController<MaterialContractOperateTurnoverService, MaterialContractOperateTurnover> {
	@Autowired
	private MaterialContractOperateTurnoverService materialContractOperateTurnoverService;

	@RequestMapping("/all")
	public Object findAll(@RequestParam(required = false) String customerName,
						  @RequestParam(required = false) String customerPhone,
						  @RequestParam(required = false) String contractStatus,
						  @RequestParam(defaultValue = "0") int offset,
						  @RequestParam(defaultValue = "20") int limit){
		Map<String, Object> params = new HashMap<String, Object>();
		MapUtils.putNotNull(params, "customerName", customerName);
		MapUtils.putNotNull(params, "customerPhone", customerPhone);
		MapUtils.putNotNull(params, "contractStatus", contractStatus);
		params.put(Constants.PAGE_OFFSET, offset);
		params.put(Constants.PAGE_SIZE, limit);
		return StatusDto.buildDataSuccessStatusDto(this.materialContractOperateTurnoverService.searchScrollPage(params));
	}
}
