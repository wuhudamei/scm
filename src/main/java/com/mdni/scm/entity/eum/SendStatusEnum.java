package com.mdni.scm.entity.eum;

//发货状态
public enum SendStatusEnum {
	PENDING_INSTALLATION("待安装"), ALREADY_INSTALLED("已安装"),STAY_STORAGE("待入库"),STORAGE("已入库");
	private String label;

	SendStatusEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
