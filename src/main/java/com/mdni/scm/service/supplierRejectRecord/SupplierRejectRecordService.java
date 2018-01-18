package com.mdni.scm.service.supplierRejectRecord;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.supplierRejectRecord.SupplierRejectRecord;
import com.mdni.scm.repository.supplierRejectRecord.SupplierRejectRecordDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 刘铎 on 2017/8/11.
 */
@Service
public class SupplierRejectRecordService extends CrudService<SupplierRejectRecordDao,SupplierRejectRecord>{

    /**
     * 根据id查询是否已驳回
     * @param id
     * @return
     */
    public Long getBySourceId(Long id) {
        return entityDao.getBySourceId(id);
    }

    public String getRejectReason(Long id) {
      String rejectReason =   entityDao.getRejectReason(id);
      return rejectReason;
    }

    public List<SupplierRejectRecord> getRejectReasonBySourceId(Long id,Long sourceType) {
        List<SupplierRejectRecord> supplierRejectRecordList = entityDao.getRejectReasonBySourceId(id,sourceType);
        return supplierRejectRecordList;
    }
}
