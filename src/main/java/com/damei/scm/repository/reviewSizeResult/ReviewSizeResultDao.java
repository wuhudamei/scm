package com.damei.scm.repository.reviewSizeResult;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.reviewSizeResult.ReviewSizeResult;

import java.util.List;

@MyBatisRepository
public interface ReviewSizeResultDao extends CrudDao<ReviewSizeResult> {
    List<ReviewSizeResult> getReviewSizeResultById(Long id);
}
