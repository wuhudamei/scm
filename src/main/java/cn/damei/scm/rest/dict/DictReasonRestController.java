package cn.damei.scm.rest.dict;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.entity.dict.DictReason;
import cn.damei.scm.service.dict.DictReasonService;
@RestController
@RequestMapping("/api/dict/reason")
public class DictReasonRestController extends BaseComController<DictReasonService, DictReason> {

	@RequestMapping("/findAll")
	public Object findAll(){
		List<DictReason> list = this.service.findAll();
		return StatusDto.buildDataSuccessStatusDto(list);
	}
}
