package com.mdni.scm.service.reviewSizeNotice;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.ReviewSizeNoticeEnum;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.entity.reviewSizeNotice.ReviewSizeNotice;
import com.mdni.scm.repository.customer.CustomerContractDao;
import com.mdni.scm.repository.reviewSizeNotice.ReviewSizeNoticeDao;
import com.mdni.scm.service.operatorLog.OperateLogService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Collections3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <dl>
 * <dd>描述: 复尺申请service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年7月28日 下午1:13:51</dd>
 * <dd>创建人： Mark</dd>
 * </dl>
 */
@Service
public class ReviewSizeNoticeService extends CrudService<ReviewSizeNoticeDao, ReviewSizeNotice> {

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private CustomerContractDao customerContractDao;

    public List<ReviewSizeNotice> getByContractId(String contractId) {
       List<ReviewSizeNotice> reviewSizeNoticeList =  this.entityDao.getByContractId(contractId);
       if(Collections3.isEmpty(reviewSizeNoticeList)){
           return new ArrayList<>();
       }
       return reviewSizeNoticeList;
    }

    public ReviewSizeNotice getContract(@Param("contractId") String contractId, @Param("id") String id) {
        ReviewSizeNotice reviewSizeNotice = this.entityDao.getContract(contractId, id);
        return reviewSizeNotice;
    }

    /**
     * 更新复尺通知单状态
     * @param id
     * @param reviewStatus
     */
    @Transactional
    public void updateReviewStatus(Long id,ReviewSizeNoticeEnum reviewStatus) {
        try {
            ReviewSizeNotice reviewSizeNotice  = this.entityDao.getById(id);
            //更新状态
            reviewSizeNotice.setReviewStatus(reviewStatus);

            OperateLog operateLog = new OperateLog();
            operateLog.setOperator( WebUtils.getLoggedUser().getName() );
            operateLog.setOperatorTime(new Date());
            operateLog.setContractCode( customerContractDao.getContractCodeById(reviewSizeNotice.getContractId()) );
            if( reviewStatus.equals( ReviewSizeNoticeEnum.NORIVEEWSIZE )   ){
                operateLog.setOperatorExplain("重新发起复尺通知");
                reviewSizeNotice.setNoticeTime(new Date());
            }else if( reviewStatus.equals( ReviewSizeNoticeEnum.YESRIVEEWSIZE )   ){
                operateLog.setOperatorExplain("供应商填报复尺结果，完成复尺操作");
            }else {
                operateLog.setOperatorExplain("供应商驳回复尺通知");
            }
            operateLogService.insert(operateLog);

            this.entityDao.updateReviewStatus(reviewSizeNotice);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
