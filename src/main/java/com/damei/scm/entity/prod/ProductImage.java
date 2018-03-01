package com.damei.scm.entity.prod;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.damei.scm.common.IdEntity;
import com.damei.scm.common.PropertyHolder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductImage extends IdEntity {

	private static final long serialVersionUID = 6558627283217550492L;

	private Long productId;

	private Long skuId;

	private String imagePath;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getFullPath() {
		if (StringUtils.isNotBlank(imagePath)) {
			return PropertyHolder.getFullImageUrl(imagePath);
		}
		return StringUtils.EMPTY;

	}

	/**
	 * 返回是否是产品主图片
	 */
	@JsonIgnore
	public boolean isProductPrimaryImg() {
		if (skuId == null || skuId < 1) {
			return true;
		}
		return false;
	}
}
