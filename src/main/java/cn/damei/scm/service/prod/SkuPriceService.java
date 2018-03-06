package cn.damei.scm.service.prod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.damei.scm.entity.eum.PriceTypeEnum;
import cn.damei.scm.entity.prod.SkuPrice;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.repository.prod.SkuPriceDao;
import org.springframework.transaction.annotation.Transactional;

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
