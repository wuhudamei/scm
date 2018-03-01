package com.damei.scm.service.operatorLog;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.operateLog.OperateLog;
import com.damei.scm.repository.operateLog.OperateLogDao;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Collections3;

import java.util.ArrayList;
import java.util.List;

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