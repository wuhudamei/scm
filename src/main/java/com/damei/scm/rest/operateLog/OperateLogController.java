package com.damei.scm.rest.operateLog;

import com.google.common.collect.Maps;
import com.damei.scm.common.BaseComController;
import com.damei.scm.common.Constants;
import com.damei.scm.common.dto.BootstrapPage;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.utils.MapUtils;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.eum.AccoutTypeEnum;
import com.damei.scm.entity.operateLog.OperateLog;
import com.damei.scm.service.operatorLog.OperateLogService;
import com.damei.scm.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/operatorLog")
public class OperateLogController extends BaseComController<OperateLogService, OperateLog> {

	@Autowired
	private OperateLogService operateLogService;

	@RequestMapping(value = "/findall")
	public Object findAll(@RequestParam(required = false) String keyword,
						  @RequestParam(defaultValue = "0") int offset,
						  @RequestParam(defaultValue = "id") String orderColumn,
						  @RequestParam(defaultValue = "DESC") String orderSort,
						  @RequestParam(defaultValue = "20") int limit){
		ShiroUser loginUser = WebUtils.getLoggedUser();
		AccoutTypeEnum acctType1 = loginUser.getAcctType();
		if(acctType1==null){
			return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
		}
		Map<String, Object> paramMap = Maps.newHashMap();
		MapUtils.putNotNull(paramMap,"keyword",keyword);
		paramMap.put(Constants.PAGE_OFFSET, offset);
		paramMap.put(Constants.PAGE_SIZE, limit);
		paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
	}

}
