package com.mdni.scm.entity.orderInstallData;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;

import java.util.Date;

/**
 * Created by 刘铎 on 2017/8/28.
 */
public class OrderInstallData extends IdEntity{

    private String orderInstallImg;//供应商点击安装完成提交的图片
    private String remark;//供应商点击安装完成的说明
    private Long orderId;//订单id
    private String orderCode;//订单code
    private String contractCode;//合同code
    private String creator;//创建人
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date creatTime;//创建时间

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
