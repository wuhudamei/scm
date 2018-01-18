package com.mdni.scm.entity.eum;

public enum AccoutTypeEnum {

	MATERIAL_MANAGER("材料部总管"),MATERIAL_CLERK("材料下单员"),STORE("门店管理员"),
	REGION_SUPPLIER("区域供应商"), PROD_SUPPLIER("商品供货商"), ADMIN("管理员"),
	FINANCE("财务人员"),CHAIRMAN_FINANCE("董事长财务");

	private String label;

	AccoutTypeEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
