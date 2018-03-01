package com.damei.scm.entity.order;

import com.damei.scm.common.IdEntity;

import java.math.BigDecimal;

public class OrderItemOtherFee extends IdEntity {


    private String feeType;

    private String feeName;

    private BigDecimal feeValue;

    private Long itemId;

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
