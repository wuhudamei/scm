package com.damei.scm.entity.eum.dataArrangement;

public enum MetarialContractStatusEnum {
    NOTCHECKED("未核对"),CHECKED("已核对"),COMPLETED("已完成");
    private String label;

    MetarialContractStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
