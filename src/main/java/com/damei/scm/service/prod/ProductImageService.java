package com.damei.scm.service.prod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.prod.ProductImage;
import com.damei.scm.repository.prod.ProductImageDao;
import com.damei.scm.service.upload.UploadService;

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

	public List<ProductImage> findByProductId(Long productId) {
		if (productId == null || productId < 1) {
			return Collections.emptyList();
		}
		return this.entityDao.findByProductIdAndSkuId(productId, null);
	}

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

	public List<ProductImage> findBySkuId(Long skuId) {
		if (skuId == null || skuId < 1) {
			return Collections.emptyList();
		}

		return this.entityDao.findByProductIdAndSkuId(null, skuId);
	}

	public List<ProductImage> findProductPrimaryImages(Long productId) {
		if (productId == null || productId < 1) {
			return Collections.emptyList();
		}
		return this.entityDao.findProductMainImageList(productId);
	}

	@Override
	public void update(ProductImage productImage) {
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

		@SuppressWarnings("unchecked")
		List<Long> newProductImagesIds = Collections3.extractToList(newProductImages, ProductImage.ID_FIELD_NAME);
		for (ProductImage oldProductImage : oldProductImages) {
			if (!newProductImagesIds.contains(oldProductImage.getId())) {
				delete(oldProductImage);
			}
		}

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
