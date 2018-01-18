package com.mdni.scm.service.ReviewSizeResultService;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.reviewSizeResult.ReviewSizeResult;
import com.mdni.scm.repository.reviewSizeResult.ReviewSizeResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Collections3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <dl>
 * <dd>ReviewSizeResultService: 复尺结果Service</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/8/1</dd>
 * <dd>@author：Chaos</dd>
 * </dl>
 */
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
