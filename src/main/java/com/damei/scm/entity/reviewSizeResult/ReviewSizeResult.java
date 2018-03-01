package com.damei.scm.entity.reviewSizeResult;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.damei.scm.common.IdEntity;
import com.damei.scm.common.utils.DateUtil;

import java.math.BigDecimal;
import java.util.Date;


public class ReviewSizeResult extends IdEntity{
    private String productName;
    private String location;
    private String model;
    private String specification;
    private String unit;
    private BigDecimal count;
    private String remark;
    private String creater;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createTime;
    private String contractId;
    private Long reviewSizeNoticeId;
    private Long prodCatalogId;
    private String prodCatalogName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Long getReviewSizeNoticeId() {
        return reviewSizeNoticeId;
    }

    public Long getProdCatalogId() {
        return prodCatalogId;
    }

    public void setProdCatalogId(Long prodCatalogId) {
        this.prodCatalogId = prodCatalogId;
    }

    public void setReviewSizeNoticeId(Long reviewSizeNoticeId) {
        this.reviewSizeNoticeId = reviewSizeNoticeId;

    }

    public String getProdCatalogName() {
        return prodCatalogName;
    }

    public void setProdCatalogName(String prodCatalogName) {
        this.prodCatalogName = prodCatalogName;
    }
}
