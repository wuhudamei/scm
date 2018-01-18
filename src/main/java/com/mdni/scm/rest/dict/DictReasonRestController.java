package com.mdni.scm.rest.dict;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.entity.dict.DictReason;
import com.mdni.scm.service.dict.DictReasonService;
/**
 * 
 * 大诚若谷信息技术有限公司
 * 功能：作废原因Controller
 * 作者:张俊奎
 * 时间：2017年6月28日上午10:35:52
 */
@RestController
@RequestMapping("/api/dict/reason")
public class DictReasonRestController extends BaseComController<DictReasonService, DictReason> {

	@RequestMapping("/findAll")
	public Object findAll(){
		List<DictReason> list = this.service.findAll();
		return StatusDto.buildDataSuccessStatusDto(list);
	}
}
