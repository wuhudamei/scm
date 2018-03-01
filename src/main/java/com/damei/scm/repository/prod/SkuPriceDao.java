package com.damei.scm.repository.prod;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.eum.PriceTypeEnum;
import com.damei.scm.entity.prod.SkuPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface SkuPriceDao extends CrudDao<SkuPrice> {

	void deleteBySkuId(Long skuId);

	SkuPrice getByDate(SkuPrice productPrice);

	List<SkuPrice> findBySkuIdAndType(@Param("skuId") Long skuId, @Param("priceType") PriceTypeEnum priceType);
	
	void batchInsert(List<SkuPrice> list);

}