package com.mdni.scm.entity.eum;

//商品状态
public enum ProductStatusEnum {

	//批量导入和克隆的商品状态是 初始化状态
	 LIST("正常"), DELIST("作废");

	private String label;

	ProductStatusEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
