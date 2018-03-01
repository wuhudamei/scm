package com.damei.scm.service.prod;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.damei.scm.entity.prod.*;
import com.damei.scm.repository.prod.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.prod.SkuApprovalRecord;

@Service
public class SkuService extends CrudService<SkuDao, Sku> {

	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private SkuPriceDao skuPriceDao;
	@Autowired
	private SkuApprovalRecordDao skuApprovalRecordDao;
	@Autowired
	private ProductDao  productDao;
	@Autowired
    private SkuMetaDao skuMetaDao;
	@Autowired
	private CatalogDao catalogDao;
	@Transactional(rollbackFor = Exception.class)
	public void deleteByProductId(Long productId) {
		List<Sku> toDeleteSkus = findByProductId(productId);
		for (Sku sku : toDeleteSkus) {
			this.delete(sku);
		}
	}

	public List<Sku> findByProductId(Long productId) {
		if (productId == null || productId < 1) {
			return Collections.emptyList();
		}
		return this.entityDao.findByProductId(productId);
	}

	public Long getSkuIdByAttributes(Map<String, Object> parameters) {
		return this.entityDao.getSkuIdByAttributes(parameters);
	}

	public boolean isSkuCodeExists(Sku sku) {
		return this.entityDao.getByCode(sku) != null;
	}

	public List<Sku> findByIdIn(List<Long> skuIdList) {
		if (CollectionUtils.isNotEmpty(skuIdList)) {
			return Collections.emptyList();
		}

		return this.entityDao.findByIdIn(skuIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(Sku sku) {
		sku.setName(sku.getProductFullName());
		this.entityDao.update(sku);
		List<ProductImage> oldSkuImages = productImageService.findBySkuId(sku.getId());
		productImageService.update(oldSkuImages, sku.getProductImages(), sku.getProduct().getId(), sku.getId());
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateSku(Sku sku){
		this.entityDao.update(sku);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(Sku sku) {
		sku.setName(sku.getProductFullName());
		this.entityDao.insert(sku);

		if (CollectionUtils.isNotEmpty(sku.getProductImages())) {
			for (ProductImage productImage : sku.getProductImages()) {
				productImage.setSkuId(sku.getId());
				productImage.setProductId(sku.getProduct().getId());
				productImageService.insert(productImage);
			}
		}
	}
	@Transactional(rollbackFor = Exception.class)
	public void delete(Sku sku) {
		if (sku == null) {
			return;
		}

		if (CollectionUtils.isNotEmpty(sku.getProductImages())) {
			for (ProductImage productImage : sku.getProductImages()) {
				productImageService.delete(productImage);
			}
		}

		this.entityDao.deleteById(sku.getId());
		this.skuPriceDao.deleteBySkuId(sku.getId());
	}

	public void incrDecrStock(long skuId, int incrDecrement) {
		if (incrDecrement == 0 || skuId < 1) {
			return;
		}

		this.entityDao.incrDecrStock(skuId, incrDecrement);
	}

    public Long countTotal(Map<String, Object> params) {
		return entityDao.countTotal(params);
    }
	public List<Sku> findApproveList(Map<String, Object> params){
		return  this.entityDao.findApproveList(params);
	}
	@Transactional(rollbackFor = Exception.class)
	public void updateProcess(Sku entity,String result,String remarks,String userId,String node) {
		Long id = entity.getId();
		Date date = new Date();
		SkuApprovalRecord skuApprovalRecord = new SkuApprovalRecord();
		skuApprovalRecord.setSkuId(id);
		skuApprovalRecord.setApprovalTime(date);
		skuApprovalRecord.setApprovalAccount(userId);
		skuApprovalRecord.setApprovalNote(remarks);
		skuApprovalRecord.setApprovalResult(result);
		skuApprovalRecord.setApprovalNode(node);
		skuApprovalRecordDao.insert(skuApprovalRecord);
		this.entityDao.update(entity);
	}


	public Sku getByInfoById(Long id) {
		Sku byId = entityDao.getById(id);
		Product byInfoById = productDao.getByInfoById(byId.getProduct().getId());
		byInfoById.setCatalog(catalogDao.getById(byInfoById.getCatalog().getId()));
		byId.setProduct(byInfoById);
        byId.setSkuMeta(skuMetaDao.getByProductId(byId.getProduct().getId()));
		List<SkuPrice> bySkuIdAndType = skuPriceDao.findBySkuIdAndType(id, null);
		byId.setPriceList(bySkuIdAndType);
		return byId;
	}


	public List<Sku> findCheckList(Map<String, Object> params) {
		return entityDao.findCheckList(params);
	}
	public Long checkTotal(Map<String, Object> params){
		return entityDao.checkTotal(params);
	}
	@Transactional(rollbackFor = Exception.class)
	public void batchSku(Sku sku) {
		entityDao.batchSku(sku);
	}
}