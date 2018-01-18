package com.mdni.scm.entity.prod;

import java.util.List;

import com.mdni.scm.common.utils.StringUtils;
import com.mdni.scm.rest.prod.SkuRestController;
import org.springframework.data.annotation.Transient;

import com.google.common.collect.Lists;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.dict.DictMeasureUnit;
import com.mdni.scm.entity.eum.ProductStatusEnum;

/**
 * <dl>
 * <dd>描述:商品</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月15日 下午1:33:58</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
public class Product extends IdEntity {

	private static final long serialVersionUID = -2130891618358544408L;

	// 商品编码
	private String code;

	// 名称
	private String name;

	//商品供货商
	private Supplier supplier;

	// 分类 
	private Catalog catalog;

	// 品牌
	private Brand brand;

	//计量单位
	private DictMeasureUnit measureUnit;

	//型号
	private String model;

	//规格
	private String spec;

	// 详情 
	private String detail;

	//此商品是否有sku
	private Boolean hasSku;

	//商品状态
	private ProductStatusEnum status;

	@Transient
	private SkuMeta skuMeta;

	@Transient
	private List<ProductImage> productImages;

	//如果商品没有sku,则把主商品做为一个sku,插入1个sku
	@Transient
	private List<Sku> skus;

	private String measureUnitName;
	//商品是否可作废 0 不可以 1 可以
	private String processStatus;

	//商品是否可以下架、作废 0 不可以 1 可以
	private String shelfMark;

	public String getProcessStatus() {
		return processStatus;
	}
	//设置
	public void setProcessStatus(String processStatus) {
		String temp="1";
		String temp2="0";
		if(!StringUtils.isEmpty(processStatus) &&
				(processStatus.contains(SkuRestController.SKU_SUPPLIER_AUDIT)||
						processStatus.contains(SkuRestController.SKU_YELLOW_CHECK)||
						processStatus.contains(SkuRestController.SKU_STORE_PURCHASE)||
						processStatus.contains(SkuRestController.SKU_CHECK_PURCHASE)||
						processStatus.contains(SkuRestController.SKU_STORE_SALE)||
						processStatus.contains(SkuRestController.SKU_CHECK_SALE)||
						processStatus.contains(SkuRestController.SKU_SHELF_FAILURE)||
						processStatus.contains(SkuRestController.SKU_SHELF_SHELVES)
				)){
			temp="0";

		}
		if(StringUtils.isNotEmpty(processStatus) &&
				(processStatus.contains(SkuRestController.SKU_SHELF_SHELVES))){
			temp2="1";
		}

		this.processStatus =temp;
		this.shelfMark =temp2;
	}

	public String getShelfMark() {
		return shelfMark;
	}

	public void setShelfMark(String shelfMark) {
		this.shelfMark = shelfMark;
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

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public DictMeasureUnit getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(DictMeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public SkuMeta getSkuMeta() {
		return skuMeta;
	}

	public void setSkuMeta(SkuMeta skuMeta) {
		this.skuMeta = skuMeta;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public Boolean getHasSku() {
		return hasSku;
	}

	public void setHasSku(Boolean hasSku) {
		this.hasSku = hasSku;
	}

	public void addSku(Sku sku) {
		if (sku == null) {
			return;
		}
		if (skus == null) {
			skus = Lists.newArrayList();
		}
		skus.add(sku);
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public ProductStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ProductStatusEnum status) {
		this.status = status;
	}

	public String getMeasureUnitName() {
		return measureUnitName;
	}

	public void setMeasureUnitName(String measureUnitName) {
		this.measureUnitName = measureUnitName;
	}
}