package com.mdni.scm.entity.review;

import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.eum.ReviewSizeNoticeEnum;

import java.util.Date;

/**
 * 复尺结果实体类
 * Created by 巢帅 on 2017/9/19.
 */
public class Review extends IdEntity {
    /**申请复尺时间**/
    private Date noticeTime;
    /**复尺内容**/
    private String name;
    /**供应商复尺时间**/
    private Date createTime;
    /**复尺状态**/
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
