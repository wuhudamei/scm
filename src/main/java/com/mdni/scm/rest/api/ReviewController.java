package com.mdni.scm.rest.api;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.entity.review.Review;
import com.mdni.scm.service.review.ReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 复尺结果controller:供综管系统台账功能调用
 * Created by 巢帅 on 2017/9/19.
 */
@RestController
@RequestMapping(value = "/externalApi/review")
public class ReviewController extends BaseComController<ReviewService, Review> {

    /**
     * 查询复尺结果
     *
     * @param contractNo 合同编号
     */
    @RequestMapping(value = "/findReview", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object findReview(@RequestParam(required = false) String contractNo) {
        return this.service.findReview(contractNo);
    }
}
