package cn.damei.scm.entity.supply;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.entity.account.User;
import cn.damei.scm.entity.eum.PayStatusEnum;
import cn.damei.scm.entity.eum.PlaceEnum;
import cn.damei.scm.entity.eum.SendStatusEnum;
import cn.damei.scm.entity.prod.Sku;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cn.damei.scm.common.utils.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Supply extends IdEntity {


    private User creator;


    private PlaceEnum placeStatus;


    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createTime;


    private String branchNo;


    private String contractCode;


    private BigDecimal quantity;

    private String installationLocation;


    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date noticeInstallDate;


    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date actualInstallDate;

    private SendStatusEnum sendStatus;


    private PayStatusEnum payStatus;


    private Sku sku;


    private String model;


    private String spec;


    private String attribute1;

    private String attribute2;

    private String attribute3;

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