package cn.damei.scm.repository.operateLog;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.operateLog.OperateLog;

import java.util.List;

@MyBatisRepository
public interface OperateLogDao extends CrudDao<OperateLog> {

    List<OperateLog> findByorderIdAndcontractId(String orderId, String contractId);
}