package cn.damei.scm.repository.reviewSizeResult;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.reviewSizeResult.ReviewSizeResult;
import cn.damei.scm.common.MyBatisRepository;

import java.util.List;

@MyBatisRepository
public interface ReviewSizeResultDao extends CrudDao<ReviewSizeResult> {
    List<ReviewSizeResult> getReviewSizeResultById(Long id);
}
