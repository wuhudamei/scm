package com.mdni.scm.repository.prod;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prod.SkuApprovalRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @describe sku审批记录 Dao
 * @author Ryze
 * @date 2017-8-14 18:56:15
 *
 */
@MyBatisRepository
public interface SkuApprovalRecordDao extends CrudDao<SkuApprovalRecord> {
    /**
     * 根据sku_id 查询sku审核记录
     * @param skuId
     * @return
     */
    List<SkuApprovalRecord> findBySkuId(Long skuId);
    /**
     * 根据sku_id  userId查询sku审核记录
     * @param skuId
     * @return
     */
    List<SkuApprovalRecord> findBySkuIdAndUserId(@Param("skuId") Long skuId, @Param("userId") String userId);
    /**
     * 根据sku_id Status 查询sku审核记录
     * @param skuId
     * @return
     */
    List<SkuApprovalRecord> findResultByStatus(@Param("skuId")Long skuId,@Param("status") String status);

    /**
     * 根据sku_id Status 查询sku 驳回审核记录条数
     * @param skuId
     * @return
     */
    Long countBackByStatus(@Param("skuId")Long skuId, @Param("status")String status);
}