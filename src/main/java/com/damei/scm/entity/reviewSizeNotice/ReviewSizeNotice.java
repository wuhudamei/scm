package com.damei.scm.entity.reviewSizeNotice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.damei.scm.common.IdEntity;
import com.damei.scm.common.utils.DateUtil;
import com.damei.scm.entity.eum.ReviewSizeNoticeEnum;
import com.damei.scm.entity.prod.Brand;
import com.damei.scm.entity.prod.Supplier;

import java.util.Date;

public class ReviewSizeNotice extends IdEntity{
    private String supplierId;
    private Supplier supplier;
    private Long contractId;
    private Brand brand;
    private String brandId;
    private String orderId;
    private String remark;
    private String createName;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createTime;
    private ReviewSizeNoticeEnum reviewStatus;
    private String reviewSizeNoticeImage;
    private String customerName;
    private String contractCode;
    private String orderCode;
    private String creatorName;
    private Integer prodCataLogId;
    private String prodCataLogName;
    private String brandName;
    private String supplierName;
    private String rejectType;
    private String uploadUrl;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date noticeTime;

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getReviewSizeNoticeImage() {
        return reviewSizeNoticeImage;
    }

    public void setReviewSizeNoticeImage(String reviewSizeNoticeImage) {
        this.reviewSizeNoticeImage = reviewSizeNoticeImage;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
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

    public Supplier getSupplier() {return supplier;}

    public void setSupplier(Supplier supplier) {this.supplier = supplier;}

    public Brand getBrand() {return brand; }

    public void setBrand(Brand brand) {this.brand = brand;}

    public Integer getProdCataLogId() {
        return prodCataLogId;
    }

    public void setProdCataLogId(Integer prodCataLogId) {
        this.prodCataLogId = prodCataLogId;
    }

    public String getProdCataLogName() {
        return prodCataLogName;
    }

    public void setProdCataLogName(String prodCataLogName) {
        this.prodCataLogName = prodCataLogName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getRejectType() {
        return rejectType;
    }

    public void setRejectType(String rejectType) {
        this.rejectType = rejectType;
    }

    public Date getNoticeTime() {return noticeTime;}

    public void setNoticeTime(Date noticeTime) {this.noticeTime = noticeTime;}
}
