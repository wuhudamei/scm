package com.damei.scm.entity.prod;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springside.modules.utils.Collections3;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.damei.scm.common.IdEntity;
import com.damei.scm.common.utils.DateUtil;
import com.damei.scm.entity.eum.PriceTypeEnum;

public class SkuPrice extends IdEntity implements Comparable<SkuPrice> {

	private static final long serialVersionUID = -1497473006583093026L;


	private Long skuId;

	private PriceTypeEnum priceType;


	private BigDecimal price;

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

	public static SkuPrice calcCurrentPrice(List<SkuPrice> priceList) {
		if (CollectionUtils.isEmpty(priceList)) {
			return null;
		}

		Date now = new Date();

		for (int i = priceList.size() - 1; i >= 0; i--) {
			SkuPrice price = priceList.get(i);

			if (now.getTime() >= price.getPriceStartDate().getTime()) {
				return price;
			}
		}
		return Collections3.getFirst(priceList);
	}

	public Boolean isEditable() {
		if (this.priceStartDate == null) {
			return null;
		}
		return this.priceStartDate.getTime() > System.currentTimeMillis();
	}

	@Override
	public int compareTo(SkuPrice o) {

		return this.priceStartDate.compareTo(o.getPriceStartDate());
	}
}
