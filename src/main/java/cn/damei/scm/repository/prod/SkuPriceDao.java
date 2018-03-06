package cn.damei.scm.repository.prod;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.eum.PriceTypeEnum;
import cn.damei.scm.entity.prod.SkuPrice;
import cn.damei.scm.common.MyBatisRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface SkuPriceDao extends CrudDao<SkuPrice> {

	void deleteBySkuId(Long skuId);

	SkuPrice getByDate(SkuPrice productPrice);

	List<SkuPrice> findBySkuIdAndType(@Param("skuId") Long skuId, @Param("priceType") PriceTypeEnum priceType);
	
	void batchInsert(List<SkuPrice> list);

}