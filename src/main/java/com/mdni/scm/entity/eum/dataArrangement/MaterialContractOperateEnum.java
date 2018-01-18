package com.mdni.scm.entity.eum.dataArrangement;

/**
 * Created by 巢帅 on 2017/8/4.
 */
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
