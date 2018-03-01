package com.damei.scm.repository.prod;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.prod.SkuMeta;

import java.util.List;

@MyBatisRepository
public interface SkuMetaDao extends CrudDao<SkuMeta> {

	SkuMeta getByProductId(Long productId);

	void deleteByProductId(Long productId);
	
	
	void batchInsert(List<SkuMeta> list);
}