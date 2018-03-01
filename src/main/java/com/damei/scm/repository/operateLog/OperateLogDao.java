package com.damei.scm.repository.operateLog;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.operateLog.OperateLog;

import java.util.List;

@MyBatisRepository
public interface OperateLogDao extends CrudDao<OperateLog> {

    List<OperateLog> findByorderIdAndcontractId(String orderId, String contractId);
}