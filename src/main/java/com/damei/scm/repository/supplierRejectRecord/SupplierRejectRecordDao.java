package com.damei.scm.repository.supplierRejectRecord;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.supplierRejectRecord.SupplierRejectRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface SupplierRejectRecordDao extends CrudDao<SupplierRejectRecord>{
    Long getBySourceId(Long id);

    String getRejectReason(Long id);

    List<SupplierRejectRecord> getRejectReasonBySourceId(@Param("id") Long id, @Param("sourceType") Long sourceType);
}
