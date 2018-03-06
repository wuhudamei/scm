package cn.damei.scm.entity.prod;

import java.util.List;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.common.utils.StringUtils;
import cn.damei.scm.rest.prod.SkuRestController;
import org.springframework.data.annotation.Transient;

import com.google.common.collect.Lists;
import cn.damei.scm.entity.dict.DictMeasureUnit;
import cn.damei.scm.entity.eum.ProductStatusEnum;

public class Product extends IdEntity {

	private static final long serialVersionUID = -2130891618358544408L;


	private String code;


	private String name;


	private Supplier supplier;

	private Catalog catalog;


	private Brand brand;


	private DictMeasureUnit measureUnit;


	private String model;


	private String spec;


	private String detail;


	private Boolean hasSku;


	private ProductStatusEnum status;

	@Transient
	private SkuMeta skuMeta;

	@Transient
	private List<ProductImage> productImages;

	@Transient
	private List<Sku> skus;

	private String measureUnitName;

	private String processStatus;

	private String shelfMark;

	public String getProcessStatus() {
		return processStatus;
	}

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