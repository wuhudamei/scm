package cn.damei.scm.repository.prod;

import java.util.List;

import cn.damei.scm.entity.prod.ProductImage;
import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;

@MyBatisRepository
public interface ProductImageDao extends CrudDao<ProductImage> {

	List<ProductImage> findByProductIdAndSkuId(@Param("productId") Long productId, @Param("skuId") Long skuId);

	List<ProductImage> findProductMainImageList(Long productId);

}