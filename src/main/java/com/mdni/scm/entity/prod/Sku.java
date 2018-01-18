package com.mdni.scm.entity.prod;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.util.CollectionUtils;
import org.springside.modules.utils.Collections3;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.PropertyHolder;
import com.mdni.scm.entity.eum.ProductStatusEnum;
import com.mdni.scm.rest.prod.SkuRestController;

/**
 * <dl>
 * <dd>描述: sku 单品</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年6月22日 下午4:01:04</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sku extends IdEntity {

	private static final long serialVersionUID = 168435529898857306L;
	/**
	 * sku编码
	 */
	private String code;
	/**
	 * sku名称
	 */
	private String name;

	private Product product;
	/**
	 * 所属供应商id 冗余字段
	 */
	private Long supplierId;
	/**
	 * 属性值1 例如：白色
	 */
	private String attribute1;
	/**
	 * 属性值2 例如：small
	 */
	private String attribute2;
	/**
	 * 属性值3 例如：300M
	 */
	private String attribute3;
	/**
	 * 库存数
	 */
	private Integer stock;
	/**
	 * sku图片
	 */
	@Transient
	private List<ProductImage> productImages;
	/**
	 * sku的价格历史记录
	 */
	private List<SkuPrice> priceList;
	/**
	 * sku属性名
	 */
	private SkuMeta skuMeta;

    public SkuMeta getSkuMeta() {
        return skuMeta;
    }

    public void setSkuMeta(SkuMeta skuMeta) {
        this.skuMeta = skuMeta;
    }

    /**
	 * 流程状态
	 */
	private String processStatus;

	/**
	 *
	 * 价格标记
	 */
	private  String priceFlag;

	/**
	 *
	 * 审批记录
	 */
	private SkuApprovalRecord skuApprovalRecord;
	/**
	 * 是否驳回
	 */
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

	//	public BigDecimal getSupplyPrice() {
	//		return supplyPrice;
	//	}
	//
	//	public void setSupplyPrice(BigDecimal supplyPrice) {
	//		this.supplyPrice = supplyPrice;
	//	}
	//
	//	public BigDecimal getStorePrice() {
	//		return storePrice;
	//	}
	//
	//	public void setStorePrice(BigDecimal storePrice) {
	//		this.storePrice = storePrice;
	//	}
	//
	//	public BigDecimal getSalePrice() {
	//		return salePrice;
	//	}
	//
	//	public void setSalePrice(BigDecimal salePrice) {
	//		this.salePrice = salePrice;
	//	}

//	public StatusEnum getStatus() {
//		return status;
//	}
//
//	public void setStatus(StatusEnum status) {
//		this.status = status;
//	}

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

	/**
	 * 是否当 前sku是否正在销售中
	 */
	@JsonIgnore
	public boolean isOnSale() {
		boolean isSaleing = false;
		if (this.product != null) {
//			isSaleing = StatusEnum.OPEN.equals(this.status) && ProductStatusEnum.LIST.equals(this.product.getStatus());
			isSaleing = SkuRestController.SKU_SHELF_SHELVES.equals(this.processStatus) && ProductStatusEnum.LIST.equals(this.product.getStatus());
		}
		return isSaleing;
	}

	// 前台显示sku对应的图片，如果sku没有上传图片，则取产品主图  
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

	/**
	 * @return 返回产品列表显示的商品名称
	 */
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