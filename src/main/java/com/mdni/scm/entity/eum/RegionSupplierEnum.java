package com.mdni.scm.entity.eum;

/**
 * Created by 刘铎 on 2017/8/3.
 */
public enum RegionSupplierEnum {
    OPEN("启用"), LOCK("停用");
    private String label;

    RegionSupplierEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
