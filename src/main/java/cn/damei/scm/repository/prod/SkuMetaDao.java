package cn.damei.scm.repository.prod;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.prod.SkuMeta;

import java.util.List;

@MyBatisRepository
public interface SkuMetaDao extends CrudDao<SkuMeta> {

	SkuMeta getByProductId(Long productId);

	void deleteByProductId(Long productId);
	
	
	void batchInsert(List<SkuMeta> list);
}