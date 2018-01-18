package com.mdni.scm.repository.operateLog;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.operateLog.OperateLog;

import java.util.List;

/**
 * <dl>
 * <dd>描述: 操作日志</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年7月26日 上午14:15</dd>
 * <dd>创建人： Mark</dd>
 * </dl>
 */
@MyBatisRepository
public interface OperateLogDao extends CrudDao<OperateLog> {

    List<OperateLog> findByorderIdAndcontractId(String orderId, String contractId);
}