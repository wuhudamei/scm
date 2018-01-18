package com.mdni.scm.repository.prod;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * <dl>
 * <dd>描述:</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017-6-23 14:46:28</dd>
 * <dd>创建人： 张俊奎</dd>
 * </dl>
 */
@MyBatisRepository
public interface BrandDao extends CrudDao<Brand> {

	Brand getByCode(Brand brand);

	Brand getByName(Brand brand);

	/**
	 * 返回某种状态的top N条品牌,品牌列表按照id升序排列
	 * 
	 * @param newStatus 品牌状态,可以为null,如果为null,则返回所有状态的品牌
	 * @param topLimit 前N条品牌,可以为null,如果为null,则返回所有品牌
	 */
	List<Brand> findTopBrandByStatusOrderByIdAsc(@Param("status") StatusEnum status, @Param("topN") Integer topLimit);

	Integer getByBrandName(String brand);

	List<Map<String,Object>> findAllForMap();
	
	List<Map<String,Object>> findByName(@Param("name") String name);

    String getBrandNameById(String brandId);
}