package cn.damei.scm.entity.eum;

public enum PriceTypeEnum {
	SUPPLY("网真采购价"), STORE("门店采购价"), SALE("门店销售价")
	,UPGRADE("升级项价"),INCREASED("增项"),MINUS("减项")
	;

	private String label;

	PriceTypeEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
