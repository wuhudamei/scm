package com.mdni.scm.service.prod;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.prod.SkuApprovalRecord;
import com.mdni.scm.repository.prod.SkuApprovalRecordDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @describe sku审批记录 Service
 * @author Ryze
 * @date 2017-8-15 17:24:44
 *
 */
@Service
public class SkuApprovalRecordService extends CrudService<SkuApprovalRecordDao,SkuApprovalRecord> {
    /**
     * 根据sku_id 查询sku审核记录
     * @param skuId
     * @return
     */
   public List<SkuApprovalRecord> findBySkuId(Long skuId){
        return entityDao.findBySkuId(skuId);
    }
    /**
     * 根据sku_id userId 查询sku审核记录
     * @param skuId
     * @return
     */
   public List<SkuApprovalRecord> findBySkuIdAndUserId( Long skuId,  String userId){
        return entityDao.findBySkuIdAndUserId(skuId,userId);
    }
    /**
     * 根据sku_id Status 查询sku审核记录
     * @param skuId
     * @return
     */
    public  List<SkuApprovalRecord> findResultByStatus( Long skuId,String Status){
        return entityDao.findResultByStatus(skuId,Status);
    }
    /**
     * 根据sku_id Status 查询sku 驳回审核记录条数
     * @param skuId
     * @return
     */
    public  Long countBackByStatus( Long skuId,String Status){
        return entityDao.countBackByStatus(skuId,Status);
    }
}