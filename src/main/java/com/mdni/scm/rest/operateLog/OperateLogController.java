package com.mdni.scm.rest.operateLog;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.service.operatorLog.OperateLogService;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>描述: 操作日志Controller</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年7月26日 下午14:25</dd>
 * <dd>创建人： Mark</dd>
 * </dl>
 */
@RestController
@RequestMapping("/api/operatorLog")
public class OperateLogController extends BaseComController<OperateLogService, OperateLog> {

	@Autowired
	private OperateLogService operateLogService;

	/**
	 * 查询操作日志
	 * @param keyword 查询条件：订单号或项目号
	 */
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
