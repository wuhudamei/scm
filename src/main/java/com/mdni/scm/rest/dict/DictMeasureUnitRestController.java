package com.mdni.scm.rest.dict;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.entity.dict.DictMeasureUnit;
import com.mdni.scm.service.dict.DictMeasureUnitService;

/**
 * <dl>
 * <dd>描述:计量单位</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017-6-23 13:22:32</dd>
 * <dd>创建人： 张俊奎</dd>
 * </dl>
 */
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
