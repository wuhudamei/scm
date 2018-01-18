package com.mdni.scm.repository.prod;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prod.ProductImage;

/**
 * 
 * <dl>
 * <dd>描述:商品图片</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 上午10:40:06</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface ProductImageDao extends CrudDao<ProductImage> {

	/**
	 * 查询商品图片列表
	 * 
	 * @param productId 商品id
	 * @param skuId skuId可以为null,如果为null,则返回某个商品的所有图片，包括主图和所有sku的图片
	 */
	List<ProductImage> findByProductIdAndSkuId(@Param("productId") Long productId, @Param("skuId") Long skuId);

	/**
	 * 返回商品主图列表
	 * 
	 * @param productId 商品id
	 */
	List<ProductImage> findProductMainImageList(Long productId);

}