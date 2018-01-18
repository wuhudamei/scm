package com.mdni.scm.service.operatorLog;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.repository.operateLog.OperateLogDao;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Collections3;

import java.util.ArrayList;
import java.util.List;

/**
 * <dl>
 * <dd>描述: 操作日志Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年7月26日 下午14:20</dd>
 * <dd>创建人： Mark</dd>
 * </dl>
 */
@Service
public class OperateLogService extends CrudService<OperateLogDao, OperateLog> {


	public List<OperateLog> findByorderIdAndcontractId(String orderId, String contractId) {
		List<OperateLog> operatorLogList = entityDao.findByorderIdAndcontractId(orderId, contractId);
			if(Collections3.isEmpty(operatorLogList)){
				return new ArrayList<>();
			}
			return operatorLogList;
	}

}