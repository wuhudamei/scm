package cn.damei.scm.service.operatorLog;

import cn.damei.scm.repository.operateLog.OperateLogDao;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.operateLog.OperateLog;
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