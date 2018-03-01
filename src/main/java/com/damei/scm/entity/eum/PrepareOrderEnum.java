package com.damei.scm.entity.eum;

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
