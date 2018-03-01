package com.damei.scm.repository.prod;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.eum.StatusEnum;
import com.damei.scm.entity.prod.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BrandDao extends CrudDao<Brand> {

	Brand getByCode(Brand brand);

	Brand getByName(Brand brand);

	List<Brand> findTopBrandByStatusOrderByIdAsc(@Param("status") StatusEnum status, @Param("topN") Integer topLimit);

	Integer getByBrandName(String brand);

	List<Map<String,Object>> findAllForMap();
	
	List<Map<String,Object>> findByName(@Param("name") String name);

    String getBrandNameById(String brandId);
}