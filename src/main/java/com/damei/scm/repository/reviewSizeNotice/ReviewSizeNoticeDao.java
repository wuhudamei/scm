package com.damei.scm.repository.reviewSizeNotice;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.reviewSizeNotice.ReviewSizeNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface ReviewSizeNoticeDao  extends CrudDao<ReviewSizeNotice> {
    List<ReviewSizeNotice> getByContractId(String contractId);
    void updateReviewStatus(Long id);

    ReviewSizeNotice getContract(@Param("contractId") String contractId, @Param("id") String id);

    List<ReviewSizeNotice> getReviewSizeNorice();

    void updateReviewStatus(ReviewSizeNotice reviewSizeNotice);
}
