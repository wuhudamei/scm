package com.damei.scm.entity.prod;

import com.damei.scm.common.IdEntity;

public class SkuMeta extends IdEntity {

	private static final long serialVersionUID = -5894043419222789902L;

	private Long productId;

	private String attribute1Name;


	private String attribute2Name;

	private String attribute3Name;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getAttribute1Name() {
		return attribute1Name;
	}

	public void setAttribute1Name(String attribute1Name) {
		this.attribute1Name = attribute1Name;
	}

	public String getAttribute2Name() {
		return attribute2Name;
	}

	public void setAttribute2Name(String attribute2Name) {
		this.attribute2Name = attribute2Name;
	}

	public String getAttribute3Name() {
		return attribute3Name;
	}

	public void setAttribute3Name(String attribute3Name) {
		this.attribute3Name = attribute3Name;
	}
}
