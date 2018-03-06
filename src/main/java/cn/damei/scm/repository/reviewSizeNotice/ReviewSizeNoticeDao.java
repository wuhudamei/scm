package cn.damei.scm.repository.reviewSizeNotice;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.reviewSizeNotice.ReviewSizeNotice;
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
