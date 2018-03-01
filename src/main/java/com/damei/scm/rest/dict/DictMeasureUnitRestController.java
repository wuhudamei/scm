package com.damei.scm.rest.dict;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.damei.scm.common.BaseComController;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.entity.dict.DictMeasureUnit;
import com.damei.scm.service.dict.DictMeasureUnitService;

@RestController
@RequestMapping(value = "/api/dict/measure")
public class DictMeasureUnitRestController extends BaseComController<DictMeasureUnitService, DictMeasureUnit> {

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Object save(DictMeasureUnit measureUnit) {
		this.service.saveOrUpdate(measureUnit);
		return StatusDto.buildSuccessStatusDto("编辑成功！");
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Object findAll() {
		return StatusDto.buildDataSuccessStatusDto(service.findAll());
	}

}
