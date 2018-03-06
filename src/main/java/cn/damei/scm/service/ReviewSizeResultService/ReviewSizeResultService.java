package cn.damei.scm.service.ReviewSizeResultService;

import cn.damei.scm.entity.reviewSizeResult.ReviewSizeResult;
import cn.damei.scm.repository.reviewSizeResult.ReviewSizeResultDao;
import cn.damei.scm.common.service.CrudService;
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
