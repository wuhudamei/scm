package com.mdni.scm.repository.reviewSizeNotice;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.reviewSizeNotice.ReviewSizeNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 刘铎 on 2017/7/28.
 */
@MyBatisRepository
public interface ReviewSizeNoticeDao  extends CrudDao<ReviewSizeNotice> {
    List<ReviewSizeNotice> getByContractId(String contractId);
    void updateReviewStatus(Long id);

    ReviewSizeNotice getContract(@Param("contractId") String contractId, @Param("id") String id);

    List<ReviewSizeNotice> getReviewSizeNorice();

    void updateReviewStatus(ReviewSizeNotice reviewSizeNotice);
}
