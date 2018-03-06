package cn.damei.scm.entity.eum.dataArrangement;

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
