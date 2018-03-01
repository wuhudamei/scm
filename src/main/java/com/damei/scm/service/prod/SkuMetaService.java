package com.damei.scm.service.prod;

import org.springframework.stereotype.Service;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.prod.SkuMeta;
import com.damei.scm.repository.prod.SkuMetaDao;
import org.springframework.transaction.annotation.Transactional;

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
