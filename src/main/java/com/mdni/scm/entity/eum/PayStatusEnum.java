package com.mdni.scm.entity.eum;

//支付状态
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
