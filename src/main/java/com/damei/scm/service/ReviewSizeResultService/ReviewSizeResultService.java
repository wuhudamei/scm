package com.damei.scm.service.ReviewSizeResultService;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.reviewSizeResult.ReviewSizeResult;
import com.damei.scm.repository.reviewSizeResult.ReviewSizeResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewSizeResultService extends CrudService<ReviewSizeResultDao,ReviewSizeResult> {
    @Autowired
    private ReviewSizeResultDao reviewSizeResultDao;

    public List<ReviewSizeResult> getReviewSizeResultById(Long id) {
        List<ReviewSizeResult> reviewSizeResults = reviewSizeResultDao.getReviewSizeResultById(id);
        if(reviewSizeResults == null){
            return Collections.EMPTY_LIST;
        }
        return reviewSizeResults;
    }
}
