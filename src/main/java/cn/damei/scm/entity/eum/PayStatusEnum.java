package cn.damei.scm.entity.eum;

public enum PayStatusEnum {
	NOT_PAIED("待结算"), PAIED("已结算");
	private String label;

	PayStatusEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
