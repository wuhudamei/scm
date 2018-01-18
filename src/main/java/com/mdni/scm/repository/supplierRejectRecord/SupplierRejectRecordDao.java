package com.mdni.scm.repository.supplierRejectRecord;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.supplierRejectRecord.SupplierRejectRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 刘铎 on 2017/8/11.
 */
@MyBatisRepository
public interface SupplierRejectRecordDao extends CrudDao<SupplierRejectRecord>{
    Long getBySourceId(Long id);

    String getRejectReason(Long id);

    List<SupplierRejectRecord> getRejectReasonBySourceId(@Param("id") Long id, @Param("sourceType") Long sourceType);
}
