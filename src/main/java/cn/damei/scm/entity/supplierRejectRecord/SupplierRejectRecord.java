package cn.damei.scm.entity.supplierRejectRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import cn.damei.scm.common.IdEntity;
import cn.damei.scm.common.utils.DateUtil;

import java.util.Date;

public class SupplierRejectRecord extends IdEntity{

    private Long sourceId;
    private Integer sourceType;
    private String rejectReason;
    private String rejectType;
    private Integer creator;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createTime;
    private String createName;
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
