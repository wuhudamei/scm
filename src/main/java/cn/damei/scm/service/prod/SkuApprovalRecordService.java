package cn.damei.scm.service.prod;

import cn.damei.scm.entity.prod.SkuApprovalRecord;
import cn.damei.scm.repository.prod.SkuApprovalRecordDao;
import cn.damei.scm.common.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuApprovalRecordService extends CrudService<SkuApprovalRecordDao,SkuApprovalRecord> {
   public List<SkuApprovalRecord> findBySkuId(Long skuId){
        return entityDao.findBySkuId(skuId);
    }
   public List<SkuApprovalRecord> findBySkuIdAndUserId( Long skuId,  String userId){
        return entityDao.findBySkuIdAndUserId(skuId,userId);
    }
    public  List<SkuApprovalRecord> findResultByStatus( Long skuId,String Status){
        return entityDao.findResultByStatus(skuId,Status);
    }
    public  Long countBackByStatus( Long skuId,String Status){
        return entityDao.countBackByStatus(skuId,Status);
    }
}