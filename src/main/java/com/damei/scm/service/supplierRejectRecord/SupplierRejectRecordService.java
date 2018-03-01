package com.damei.scm.service.supplierRejectRecord;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.supplierRejectRecord.SupplierRejectRecord;
import com.damei.scm.repository.supplierRejectRecord.SupplierRejectRecordDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierRejectRecordService extends CrudService<SupplierRejectRecordDao,SupplierRejectRecord>{


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
