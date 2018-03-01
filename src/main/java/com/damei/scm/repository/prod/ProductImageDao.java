package com.damei.scm.repository.prod;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.prod.ProductImage;

@MyBatisRepository
public interface ProductImageDao extends CrudDao<ProductImage> {

	List<ProductImage> findByProductIdAndSkuId(@Param("productId") Long productId, @Param("skuId") Long skuId);

	List<ProductImage> findProductMainImageList(Long productId);

}