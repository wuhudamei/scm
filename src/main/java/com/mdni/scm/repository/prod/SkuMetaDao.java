package com.mdni.scm.repository.prod;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prod.SkuMeta;

import java.util.List;

/**
 * 
 * <dl>
 * <dd>描述: sku元数据</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 上午10:40:06</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface SkuMetaDao extends CrudDao<SkuMeta> {

	SkuMeta getByProductId(Long productId);

	void deleteByProductId(Long productId);
	
	
	void batchInsert(List<SkuMeta> list);
}