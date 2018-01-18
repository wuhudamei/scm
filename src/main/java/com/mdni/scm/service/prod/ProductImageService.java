package com.mdni.scm.service.prod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.prod.ProductImage;
import com.mdni.scm.repository.prod.ProductImageDao;
import com.mdni.scm.service.upload.UploadService;

/**
 * <dl>
 * <dd>描述: 产品图片Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年6月26日 上午11:25:40</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class ProductImageService extends CrudService<ProductImageDao, ProductImage> {

	@Autowired
	private UploadService uploadService;

	public void insert(ProductImage productImage) {
		productImage.setImagePath(uploadService.submitPath(productImage.getImagePath()));
		this.entityDao.insert(productImage);
	}

	public void saveUpdate(ProductImage productImage) {
		if (productImage.getId() == null) {
			insert(productImage);
		} else {
			update(productImage);
		}
	}

	/**
	 * 返回产品的所有图片，包括 主图 和 产品所有sku组合的图片
	 */
	public List<ProductImage> findByProductId(Long productId) {
		if (productId == null || productId < 1) {
			return Collections.emptyList();
		}
		return this.entityDao.findByProductIdAndSkuId(productId, null);
	}

	/**
	 * 获得商品下所有sku列表的图片
	 * 
	 * @param productId 商品Id
	 */
	public Map<Long, List<ProductImage>> findSkuImgsListMap(Long productId) {
		List<ProductImage> prodImgList = findByProductId(productId);

		Map<Long, List<ProductImage>> skuImgListMap = Maps.newHashMap();
		for (ProductImage prodImg : prodImgList) {
			if (prodImg.getSkuId() != null) {

				List<ProductImage> skuImgs = skuImgListMap.get(prodImg.getSkuId());
				if (skuImgs == null) {
					skuImgs = Lists.newArrayList();
					skuImgListMap.put(prodImg.getSkuId(), skuImgs);
				}
				skuImgs.add(prodImg);
			}
		}
		return skuImgListMap;
	}

	/**
	 * 获得某个sku的图片
	 * 
	 * @param skuId
	 */
	public List<ProductImage> findBySkuId(Long skuId) {
		if (skuId == null || skuId < 1) {
			return Collections.emptyList();
		}

		return this.entityDao.findByProductIdAndSkuId(null, skuId);
	}

	/**
	 * 返回某个产品的所有主图
	 * 
	 * @param productId 产品Id
	 */
	public List<ProductImage> findProductPrimaryImages(Long productId) {
		if (productId == null || productId < 1) {
			return Collections.emptyList();
		}
		return this.entityDao.findProductMainImageList(productId);
	}

	@Override
	public void update(ProductImage productImage) {
		//删除旧图片文件，保存新图片文件
		if (productImage.getImagePath() != null) {
			productImage.setImagePath(uploadService.submitPath(productImage.getImagePath()));

			ProductImage oldProductImage = this.entityDao.getById(productImage.getId());
			if (oldProductImage != null && oldProductImage.getImagePath() != null
				&& !oldProductImage.getImagePath().equals(productImage.getImagePath())) {
				uploadService.delete(oldProductImage.getImagePath());
			}
		}

		entityDao.update(productImage);
	}

	public void update(List<ProductImage> oldProductImages, List<ProductImage> newProductImages, Long productId,
		Long skuId) {

		if (newProductImages == null) {
			newProductImages = Collections.emptyList();
		}

		for (ProductImage productImage : newProductImages) {
			productImage.setProductId(productId);
			productImage.setSkuId(skuId);
		}

		//删除已不要的图片
		@SuppressWarnings("unchecked")
		List<Long> newProductImagesIds = Collections3.extractToList(newProductImages, ProductImage.ID_FIELD_NAME);
		for (ProductImage oldProductImage : oldProductImages) {
			if (!newProductImagesIds.contains(oldProductImage.getId())) {
				delete(oldProductImage);
			}
		}

		//更新或新增新图片
		for (ProductImage image : newProductImages) {
			saveUpdate(image);
		}
	}

	public void updateProductMainImgs(List<ProductImage> oldProductImages, List<ProductImage> newProductImages,
		Long productId) {
		update(oldProductImages, newProductImages, productId, null);
	}

	@Override
	public void deleteById(Long id) {
		delete(this.entityDao.getById(id));
	}

	public void delete(ProductImage productImage) {
		Assert.notNull(productImage.getId());
		this.entityDao.deleteById(productImage.getId());
		uploadService.delete(productImage.getImagePath());
	}
}
