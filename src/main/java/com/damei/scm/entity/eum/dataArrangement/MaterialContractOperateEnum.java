package com.damei.scm.entity.eum.dataArrangement;

public enum MaterialContractOperateEnum {
    ADD("增加"),DELETE("删除"),UPDATE("修改");
    private String label;

    MaterialContractOperateEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
