package com.mdni.scm.service.review;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.review.Review;
import com.mdni.scm.repository.review.ReviewDao;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 复尺结果service
 * Created by 巢帅 on 2017/9/19.
 */
@Service
public class ReviewService extends CrudService<ReviewDao, Review> {

    /**
     * 查询复尺结果
     *
     * @param contractNo 合同编号
     */
    public List<Review> findReview(String contractNo) {
        try {
            return this.entityDao.findReview(contractNo);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
