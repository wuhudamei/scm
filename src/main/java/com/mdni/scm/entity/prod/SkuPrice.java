package com.mdni.scm.entity.prod;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springside.modules.utils.Collections3;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.entity.eum.PriceTypeEnum;

public class SkuPrice extends IdEntity implements Comparable<SkuPrice> {

	private static final long serialVersionUID = -1497473006583093026L;

	//设置的sku
	private Long skuId;

	//价格类型
	private PriceTypeEnum priceType;

	//供货商给 区域供应商的价格
	private BigDecimal price;

	//从此时间起,按照此价格销售：即：now() >= priceStartDate
	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date priceStartDate;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Date getPriceStartDate() {
		return priceStartDate;
	}

	public void setPriceStartDate(Date priceStartDate) {
		this.priceStartDate = priceStartDate;
	}

	public PriceTypeEnum getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceTypeEnum priceType) {
		this.priceType = priceType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 计算当天 应该销售的价格
	 * 
	 * @param priceList 价格列表[已按照日期升序排列]
	 */
	public static SkuPrice calcCurrentPrice(List<SkuPrice> priceList) {
		if (CollectionUtils.isEmpty(priceList)) {
			return null;
		}

		Date now = new Date();
		//从后往前循环
		for (int i = priceList.size() - 1; i >= 0; i--) {
			SkuPrice price = priceList.get(i);
			//如果now >=  
			if (now.getTime() >= price.getPriceStartDate().getTime()) {
				return price;
			}
		}
		return Collections3.getFirst(priceList);
	}

	//商品价格是否可以编辑,如果priceStartDate > now() 则可以编辑
	public Boolean isEditable() {
		if (this.priceStartDate == null) {
			return null;
		}
		return this.priceStartDate.getTime() > System.currentTimeMillis();
	}

	@Override
	public int compareTo(SkuPrice o) {
		//价格列表 按照日期升序排列
		return this.priceStartDate.compareTo(o.getPriceStartDate());
	}
}
