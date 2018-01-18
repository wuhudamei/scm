package com.mdni.scm.entity.eum;

/**
 * @Description: 预备单状态枚举
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/28 15:11.
 */
public enum PrepareOrderEnum {

    WAITING_TRANSFERRED("待转单"),  ALREADY_TRANSFERRED("已转单"),
    HAS_NULLIFIED("已作废");

    private String label;
    PrepareOrderEnum(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
