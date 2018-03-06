package cn.damei.scm.rest.dict;

import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.service.dict.DictMeasureUnitService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.entity.dict.DictMeasureUnit;

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
