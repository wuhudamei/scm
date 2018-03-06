package cn.damei.scm.entity.eum;

public enum CatalogTypeEnum {
    PRINCIPAL_MATERIAL("主材"),AUXILIARY_MATERIAL("基、辅材"),SOFT_ACCESSORIES("软饰"),
    FURNITURE("家具"),HOME_APPLIANCES("家电");
    private String label;

    CatalogTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
