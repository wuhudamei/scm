package cn.damei.scm.entity.eum.dataArrangement;

public enum MaterialChangeDetailTypeEnum {
    INCREASE("增项"),MINUSITEM("减项"),COMPENSATE("赔付");
    private String label;
    MaterialChangeDetailTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
