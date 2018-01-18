package com.mdni.scm.repository.review;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.review.Review;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 复尺结果dao
 * Created by 巢帅 on 2017/9/19.
 */
@MyBatisRepository
public interface ReviewDao extends CrudDao<Review> {
    List<Review> findReview(@Param("contractCode") String contractNo);
}
