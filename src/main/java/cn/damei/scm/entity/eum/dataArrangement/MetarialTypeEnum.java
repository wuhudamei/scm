package cn.damei.scm.entity.eum.dataArrangement;

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
