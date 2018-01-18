package com.mdni.scm.entity.eum.dataArrangement;

/**
 * Created by 李万财 on 2017-08-04.
 */
public enum MetarialTypeEnum {
    BASE("基装"),MAIN("主材");
    private String label;

    MetarialTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
