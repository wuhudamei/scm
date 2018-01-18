package com.mdni.scm.entity.supply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.entity.account.User;
import com.mdni.scm.entity.eum.PayStatusEnum;
import com.mdni.scm.entity.eum.PlaceEnum;
import com.mdni.scm.entity.eum.SendStatusEnum;
import com.mdni.scm.entity.prod.Sku;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <dl>
 * <dd>描述：商品供货商</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 下午2:29:59</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Supply extends IdEntity {

    //创建人/制单人
    private User creator;

    //制单类型
    private PlaceEnum placeStatus;

    // 创建时间
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createTime;

    //批次号
    private String branchNo;

    //合同号
    private String contractCode;

    //订货数量
    private BigDecimal quantity;

    //安装位置
    private String installationLocation;

    //通知安装时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date noticeInstallDate;

    //实际安装时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date actualInstallDate;

    /**
     * 发货状态
     **/
    private SendStatusEnum sendStatus;

    /**
     * 支付状态
     */
    private PayStatusEnum payStatus;

    //订货的商品
    private Sku sku;

    /**
     * 商品型号
     **/
    private String model;

    /**
     * 商品规格
     **/
    private String spec;

    /**
     * 商品属性1
     **/
    private String attribute1;
    /**
     * 商品属性2
     **/
    private String attribute2;
    /**
     * 商品属性3
     **/
    private String attribute3;

    /**
     * 商品sku名称
     **/
    private String skuName;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public PlaceEnum getPlaceStatus() {
        return placeStatus;
    }

    public void setPlaceStatus(PlaceEnum placeStatus) {
        this.placeStatus = placeStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getInstallationLocation() {
        return installationLocation;
    }

    public void setInstallationLocation(String installationLocation) {
        this.installationLocation = installationLocation;
    }

    public Date getNoticeInstallDate() {
        return noticeInstallDate;
    }

    public void setNoticeInstallDate(Date noticeInstallDate) {
        this.noticeInstallDate = noticeInstallDate;
    }

    public Date getActualInstallDate() {
        return actualInstallDate;
    }

    public void setActualInstallDate(Date actualInstallDate) {
        this.actualInstallDate = actualInstallDate;
    }

    public SendStatusEnum getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatusEnum sendStatus) {
        this.sendStatus = sendStatus;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}