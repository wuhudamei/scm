package cn.damei.scm.entity.eum;

public enum PlaceEnum {
    NORMAL("正常下单"),CHANGE("变更单");
    private String label;

    PlaceEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
