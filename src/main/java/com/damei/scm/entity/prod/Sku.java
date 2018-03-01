package com.damei.scm.entity.prod;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.util.CollectionUtils;
import org.springside.modules.utils.Collections3;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.damei.scm.common.IdEntity;
import com.damei.scm.common.PropertyHolder;
import com.damei.scm.entity.eum.ProductStatusEnum;
import com.damei.scm.rest.prod.SkuRestController;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sku extends IdEntity {

	private static final long serialVersionUID = 168435529898857306L;

	private String code;

	private String name;

	private Product product;

	private Long supplierId;

	private String attribute1;

	private String attribute2;

	private String attribute3;

	private Integer stock;

	@Transient
	private List<ProductImage> productImages;

	private List<SkuPrice> priceList;

	private SkuMeta skuMeta;

    public SkuMeta getSkuMeta() {
        return skuMeta;
    }

    public void setSkuMeta(SkuMeta skuMeta) {
        this.skuMeta = skuMeta;
    }


	private String processStatus;


	private  String priceFlag;


	private SkuApprovalRecord skuApprovalRecord;

	private Long backFlag;

	public Long getBackFlag() {
		return backFlag;
	}

	public void setBackFlag(Long backFlag) {
		this.backFlag = backFlag;
	}

	public SkuApprovalRecord getSkuApprovalRecord() {
		return skuApprovalRecord;
	}

	public void setSkuApprovalRecord(SkuApprovalRecord skuApprovalRecord) {
		this.skuApprovalRecord = skuApprovalRecord;
	}

	public String getPriceFlag() {
		return priceFlag;
	}

	public void setPriceFlag(String priceFlag) {
		this.priceFlag = priceFlag;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public List<SkuPrice> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<SkuPrice> priceList) {
		this.priceList = priceList;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@JsonIgnore
	public boolean isOnSale() {
		boolean isSaleing = false;
		if (this.product != null) {
			isSaleing = SkuRestController.SKU_SHELF_SHELVES.equals(this.processStatus) && ProductStatusEnum.LIST.equals(this.product.getStatus());
		}
		return isSaleing;
	}

	public String getPreviewImagePath() {
		if (productImages != null && productImages.size() > 0) {
			return PropertyHolder.getFullImageUrl(Collections3.getFirst(productImages).getImagePath());
		}

		if (this.product != null && !CollectionUtils.isEmpty(this.product.getProductImages())) {
			return PropertyHolder
				.getFullImageUrl(Collections3.getFirst(this.product.getProductImages()).getImagePath());
		}
		return StringUtils.EMPTY;
	}

	@JsonIgnore
	public String getProductFullName() {
		if (product != null && StringUtils.isNotBlank(product.getName())) {
			final String blank = " ";
			StringBuilder nameBuilder = new StringBuilder(product.getName());
			if (StringUtils.isNotBlank(attribute1)) {
				nameBuilder.append(blank).append(attribute1);
			}
			if (StringUtils.isNotBlank(attribute2)) {
				nameBuilder.append(blank).append(attribute2);
			}
			if (StringUtils.isNotBlank(attribute3)) {
				nameBuilder.append(blank).append(attribute3);
			}
			return nameBuilder.toString();
		} else {
			return StringUtils.EMPTY;
		}
	}

}