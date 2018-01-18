package com.mdni.scm.entity.prod;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.PropertyHolder;

/**
 * <dl>
 * <dd>描述:商品图片</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月15日 下午1:33:58</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
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
