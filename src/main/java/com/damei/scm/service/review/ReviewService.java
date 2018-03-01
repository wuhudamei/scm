package com.damei.scm.service.review;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.review.Review;
import com.damei.scm.repository.review.ReviewDao;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewService extends CrudService<ReviewDao, Review> {


    public List<Review> findReview(String contractNo) {
        try {
            return this.entityDao.findReview(contractNo);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
