package cn.damei.scm.entity.eum;

public enum ReconciliationStatusEnum {
    NOT_RECONCILIATION("未对账"),RECONCILIATION("已对账");
    private String label;
    ReconciliationStatusEnum(String label){
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
