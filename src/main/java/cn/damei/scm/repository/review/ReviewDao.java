package cn.damei.scm.repository.review;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.review.Review;
import cn.damei.scm.common.MyBatisRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface ReviewDao extends CrudDao<Review> {
    List<Review> findReview(@Param("contractCode") String contractNo);
}
