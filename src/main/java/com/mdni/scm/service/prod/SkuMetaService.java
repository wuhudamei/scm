package com.mdni.scm.service.prod;

import org.springframework.stereotype.Service;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.prod.SkuMeta;
import com.mdni.scm.repository.prod.SkuMetaDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * <dl>
 * <dd>描述: sku元数据Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月15日 下午2:54:29</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class SkuMetaService extends CrudService<SkuMetaDao, SkuMeta> {

	public SkuMeta getByProductId(Long productId) {
		if (productId == null || productId < 1) {
			return null;
		}

		return this.entityDao.getByProductId(productId);
	}
	@Transactional(rollbackFor = Exception.class)
	public void deleteByProductId(Long productId) {
		if (productId != null && productId > 0) {
			this.entityDao.deleteByProductId(productId);
		}
	}

}
