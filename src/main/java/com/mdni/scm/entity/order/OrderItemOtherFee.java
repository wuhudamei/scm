package com.mdni.scm.entity.order;

import com.mdni.scm.common.IdEntity;

import java.math.BigDecimal;

/**
 * Created by 闪电侠 on .
 */
/**
 * <dl>
 * <dd>描述: 订货单详细下的其他费用 实体</dd>
 * <dd>创建时间：2017/8/18 下午16:25:00</dd>
 * <dd>创建人： Paul</dd>
 * </dl>
 */
public class OrderItemOtherFee extends IdEntity {

    //费用id,来自字典表
    private String feeType;
    //费用名称
    private String feeName;
    //费用值
    private BigDecimal feeValue;
    //对应 订货单项item的id
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
