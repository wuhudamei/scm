package com.mdni.scm.entity.reviewSizeNotice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.entity.eum.ReviewSizeNoticeEnum;
import com.mdni.scm.entity.prod.Brand;
import com.mdni.scm.entity.prod.Supplier;

import java.util.Date;

/**
 * Created by 刘铎 on 2017/7/28.
 */
public class ReviewSizeNotice extends IdEntity{
    private String supplierId;//供应商id
    private Supplier supplier;
    private Long contractId;//项目id
    private Brand brand;
    private String brandId;//品牌id
    private String orderId;//订单id
    private String remark;//说明
    private String createName;//创建人
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createTime;//创建时间
    private ReviewSizeNoticeEnum reviewStatus;//复尺状态  0：待复尺，1：已复尺
    private String reviewSizeNoticeImage;//复尺图片
    private String customerName;//客户姓名
    private String contractCode;//项目编号
    private String orderCode;//订单编号
    private String creatorName;//制单人
    private Integer prodCataLogId;//类目id
    private String prodCataLogName;//类目名称
    private String brandName;//品牌名称
    private String supplierName;//供应商名称
    private String rejectType;//驳回类型
    //上传文件 路径  null 未上传
    private String uploadUrl;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date noticeTime;   //通知复尺时间

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
