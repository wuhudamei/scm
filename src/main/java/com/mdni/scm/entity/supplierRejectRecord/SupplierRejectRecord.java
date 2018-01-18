package com.mdni.scm.entity.supplierRejectRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;

import java.util.Date;

/**
 * Created by 刘铎 on 2017/8/11.
 */
public class SupplierRejectRecord extends IdEntity{

    private Long sourceId;//订货单id/复尺单id
    private Integer sourceType;//类型：1是订货单，2是复尺单
    private String rejectReason;//驳回原因
    private String rejectType;//驳回类型
    private Integer creator;//创建人
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createTime;//创建时间
    private String createName;//创建人，返回用
    private String indentOrderRejectType;

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getRejectType() {
        return rejectType;
    }

    public void setRejectType(String rejectType) {
        this.rejectType = rejectType;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getIndentOrderRejectType() {
        return indentOrderRejectType;
    }

    public void setIndentOrderRejectType(String indentOrderRejectType) {
        this.indentOrderRejectType = indentOrderRejectType;
    }
}
