package com.damei.scm.rest.api;

import com.damei.scm.common.BaseComController;
import com.damei.scm.entity.review.Review;
import com.damei.scm.service.review.ReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/externalApi/review")
public class ReviewController extends BaseComController<ReviewService, Review> {

    @RequestMapping(value = "/findReview", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object findReview(@RequestParam(required = false) String contractNo) {
        return this.service.findReview(contractNo);
    }
}
