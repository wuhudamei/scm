package com.damei.scm.entity.eum.dataArrangement;

public enum MaterialChangeDetailChangeTypeEnum {
    CHANGESUBJECTMATERIAL("主材变更"),BASECHANGE("基装变更");
    private String label;
    MaterialChangeDetailChangeTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
