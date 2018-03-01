package com.damei.scm.repository.review;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.review.Review;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface ReviewDao extends CrudDao<Review> {
    List<Review> findReview(@Param("contractCode") String contractNo);
}
