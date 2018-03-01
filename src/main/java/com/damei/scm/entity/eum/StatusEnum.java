package com.damei.scm.entity.eum;

public enum StatusEnum {
	OPEN("启用"), LOCK("停用");
	private String label;

	StatusEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
