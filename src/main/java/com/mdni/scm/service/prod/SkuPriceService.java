package com.mdni.scm.service.prod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.eum.PriceTypeEnum;
import com.mdni.scm.entity.prod.SkuPrice;
import com.mdni.scm.repository.prod.SkuPriceDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * <dl>
 * <dd>描述: 商品价格Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年6月26日 上午11:25:40</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class SkuPriceService extends CrudService<SkuPriceDao, SkuPrice> {

	public SkuPrice getByDate(SkuPrice price) {
		if (price == null || price.getPriceStartDate() == null)
			return null;
		return this.entityDao.getByDate(price);
	}
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(SkuPrice price) {
		if (price.getId() == null) {
			insert(price);
		} else {
			update(price);
		}
	}

	/**
	 * 查询某个Sku&某个价格类型 的价格记录列表
	 * 
	 * @param skuId
	 * @param priceType 价格类型
	 */
	public List<SkuPrice> findBySkuIdAndType(Long skuId, PriceTypeEnum priceType) {
		if (skuId == null || priceType == null) {
			return Collections.emptyList();
		}
		return this.entityDao.findBySkuIdAndType(skuId, priceType);
	}

	public Map<PriceTypeEnum, List<SkuPrice>> findListMapBySkuId(Long skuId) {
		if (skuId == null) {
			return MapUtils.EMPTY_MAP;
		}

		List<SkuPrice> skuPriceList = this.entityDao.findBySkuIdAndType(skuId, null);
		return convertListToTypeMap(skuPriceList);
	}

	private Map<PriceTypeEnum, List<SkuPrice>> convertListToTypeMap(List<SkuPrice> priceList) {
		Map<PriceTypeEnum, List<SkuPrice>> typePriceListMap = Maps.newHashMap();
		for (SkuPrice price : priceList) {
			List<SkuPrice> prices = typePriceListMap.get(price.getPriceType());
			if (prices == null) {
				prices = Lists.newArrayList();
				typePriceListMap.put(price.getPriceType(), prices);
			}
			prices.add(price);
		}

		return typePriceListMap;
	}
}
