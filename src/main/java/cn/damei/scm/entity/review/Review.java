package cn.damei.scm.entity.review;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.entity.eum.ReviewSizeNoticeEnum;

import java.util.Date;

public class Review extends IdEntity {

    private Date noticeTime;

    private String name;

    private Date createTime;

    private ReviewSizeNoticeEnum reviewStatus;

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ReviewSizeNoticeEnum getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewSizeNoticeEnum reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
}
