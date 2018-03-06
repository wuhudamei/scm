package cn.damei.scm.entity.orderInstallData;

import cn.damei.scm.common.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.damei.scm.common.utils.DateUtil;

import java.util.Date;

public class OrderInstallData extends IdEntity {

    private String orderInstallImg;
    private String remark;
    private Long orderId;
    private String orderCode;
    private String contractCode;
    private String creator;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date creatTime;

    public String getOrderInstallImg() {
        return orderInstallImg;
    }

    public void setOrderInstallImg(String orderInstallImg) {
        this.orderInstallImg = orderInstallImg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
}
