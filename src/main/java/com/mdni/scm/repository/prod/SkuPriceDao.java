package com.mdni.scm.repository.prod;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.eum.PriceTypeEnum;
import com.mdni.scm.entity.prod.SkuPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * <dl>
 * <dd>描述:商品价格Dao</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 上午10:40:06</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface SkuPriceDao extends CrudDao<SkuPrice> {

	void deleteBySkuId(Long skuId);

	SkuPrice getByDate(SkuPrice productPrice);

	/**
	 * 查询某个Sku的价格记录列表
	 * 
	 * @param skuId
	 * @param priceType 价格类型,可以为null，如果为null则返回sku的各种价格列表
	 */
	List<SkuPrice> findBySkuIdAndType(@Param("skuId") Long skuId, @Param("priceType") PriceTypeEnum priceType);
	
	void batchInsert(List<SkuPrice> list);

}