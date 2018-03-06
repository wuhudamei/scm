package cn.damei.scm.repository.prod;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.prod.SkuApprovalRecord;
import cn.damei.scm.common.MyBatisRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface SkuApprovalRecordDao extends CrudDao<SkuApprovalRecord> {
    List<SkuApprovalRecord> findBySkuId(Long skuId);
    List<SkuApprovalRecord> findBySkuIdAndUserId(@Param("skuId") Long skuId, @Param("userId") String userId);
    List<SkuApprovalRecord> findResultByStatus(@Param("skuId")Long skuId,@Param("status") String status);

    Long countBackByStatus(@Param("skuId")Long skuId, @Param("status")String status);
}