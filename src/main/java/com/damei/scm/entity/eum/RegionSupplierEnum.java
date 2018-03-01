package com.damei.scm.entity.eum;

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
