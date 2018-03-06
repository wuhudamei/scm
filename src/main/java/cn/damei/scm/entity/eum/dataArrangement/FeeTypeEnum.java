package cn.damei.scm.entity.eum.dataArrangement;

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
