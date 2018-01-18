package com.mdni.scm.entity.eum.dataArrangement;

/**
 * Created by 李万财 on 2017-08-04.
 */
public enum FeeTypeEnum {
    ADD("增项"),REDUCE("减项"),COMPREHENSIVE("基装综合");
    private String label;

    FeeTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
