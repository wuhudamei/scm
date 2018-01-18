package com.mdni.scm.entity.eum.dataArrangement;

/**
 * Created by MrLi on 2017-08-15.
 */
public enum MaterialCustomerContractDataTypeEnum {
    INPUT("录入"), CHECK("核查");
    private String label;
    MaterialCustomerContractDataTypeEnum(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
