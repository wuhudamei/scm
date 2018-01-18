package com.mdni.scm.service.prod;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mdni.scm.entity.account.User;
import com.mdni.scm.entity.eum.PriceTypeEnum;
import com.mdni.scm.entity.prod.*;
import com.mdni.scm.repository.prod.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.prod.SkuApprovalRecord;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * <dl>
 * <dd>描述: sku Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 上午11:21:21</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
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

	/**
	 * 通过sku销售属性值获得skuId
	 * 
	 * @param parameters sku属性值参数：Map的key:{attribute1,attribute2,attribute3,productId}
	 */
	public Long getSkuIdByAttributes(Map<String, Object> parameters) {
		return this.entityDao.getSkuIdByAttributes(parameters);
	}

	/**
	 * 通过sku编码获得Sku
	 * 
	 * @param code sku编码
	 */
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

	/**
	 * sku上下架
	 * @param sku
	 */
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

	/**
	 * 增加或较少 sku库存数量
	 * 
	 * @param skuId
	 * @param incrDecrement 增加量或减少量 incrDecrement>0是增量; incrDecrement<0是较少量
	 */
	public void incrDecrStock(long skuId, int incrDecrement) {
		if (incrDecrement == 0 || skuId < 1) {
			return;
		}

		this.entityDao.incrDecrStock(skuId, incrDecrement);
	}

	/**
	 * @Ryze
	 * 审批列表 查条数
	 * @param params
	 * @return
	 */
    public Long countTotal(Map<String, Object> params) {
		return entityDao.countTotal(params);
    }
	/**
	 * @Ryze
	 * 审批列表
	 * @param params
	 * @return
	 */
	public List<Sku> findApproveList(Map<String, Object> params){
		return  this.entityDao.findApproveList(params);
	}
	/**
	 * @Ryze
	 * 修改流程状态
	 * @param entity
	 * @return
	 */
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
		//添加 轨迹
		skuApprovalRecordDao.insert(skuApprovalRecord);
		this.entityDao.update(entity);
	}

	/**
	 *  查询 SKU商品详细信息
	 * @param id
	 * @return
	 */

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


	/**
	 * 已审核列表查询
	 * @param params
	 * @return
	 */
	public List<Sku> findCheckList(Map<String, Object> params) {
		return entityDao.findCheckList(params);
	}
	/**
	 * @Ryze
	 * 已审批列表 查条数
	 * @param params
	 * @return
	 */
	public Long checkTotal(Map<String, Object> params){
		return entityDao.checkTotal(params);
	}
	//根据商品Id 修改Sku 状态
	@Transactional(rollbackFor = Exception.class)
	public void batchSku(Sku sku) {
		entityDao.batchSku(sku);
	}
}