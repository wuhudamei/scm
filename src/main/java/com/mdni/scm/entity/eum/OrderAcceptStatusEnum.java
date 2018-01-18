package com.mdni.scm.entity.eum;

//备货单接收状态
public enum OrderAcceptStatusEnum {

	YES("接收"), NO("未接收");

	private String label;

	OrderAcceptStatusEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
