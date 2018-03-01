package com.damei.scm.rest.dict;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.damei.scm.common.BaseComController;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.entity.dict.DictReason;
import com.damei.scm.service.dict.DictReasonService;
@RestController
@RequestMapping("/api/dict/reason")
public class DictReasonRestController extends BaseComController<DictReasonService, DictReason> {

	@RequestMapping("/findAll")
	public Object findAll(){
		List<DictReason> list = this.service.findAll();
		return StatusDto.buildDataSuccessStatusDto(list);
	}
}
