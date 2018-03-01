package com.damei.scm.repository.prod;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.prod.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SkuDao extends CrudDao<Sku> {

	List<Sku> findByIdIn(List<Long> skuIdList);

	List<Sku> findByProductId(Long productId);

	Long getSkuIdByAttributes(Map<String, Object> parameters);

	Sku getByCode(Sku sku);

	void incrDecrStock(@Param("skuId") Long skuId, @Param("incrDecrement") int incrDecrement);

    Long countTotal(Map<String, Object> params);
	List<Sku> findApproveList(Map<String, Object> params);

	List<Sku> findCheckList(Map<String, Object> params);


	Long checkTotal(Map<String, Object> params);
    void batchSku(Sku sku);
	
	List<Sku> insertBatchReturnProperty(List<Sku> list);
}