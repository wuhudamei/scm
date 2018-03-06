package cn.damei.scm.repository.supplierRejectRecord;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.supplierRejectRecord.SupplierRejectRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface SupplierRejectRecordDao extends CrudDao<SupplierRejectRecord> {
    Long getBySourceId(Long id);

    String getRejectReason(Long id);

    List<SupplierRejectRecord> getRejectReasonBySourceId(@Param("id") Long id, @Param("sourceType") Long sourceType);
}
