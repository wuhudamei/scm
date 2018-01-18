package com.mdni.scm.entity.reviewSizeNotice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.PropertyHolder;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewSizeNoticeImage extends IdEntity {

	private static final long serialVersionUID = 6558627283217550492L;

	private Integer productId;

	private Integer skuId;

	private String imagePath;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
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
