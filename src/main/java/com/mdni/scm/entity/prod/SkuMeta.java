package com.mdni.scm.entity.prod;

import com.mdni.scm.common.IdEntity;

/**
 * <dl>
 * <dd>描述: sku元数据</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年6月22日 下午4:28:31</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
public class SkuMeta extends IdEntity {

	private static final long serialVersionUID = -5894043419222789902L;

	private Long productId;
	/**
	 * sku销售属性名称:例如颜色
	 */
	private String attribute1Name;

	/**
	 *sku销售属性名称:例如尺码
	 */
	private String attribute2Name;

	/**
	 * 	sku销售属性名称:例如内存
	 */
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
