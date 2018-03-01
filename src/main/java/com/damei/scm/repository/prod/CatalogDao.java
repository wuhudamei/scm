package com.damei.scm.repository.prod;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.eum.StatusEnum;
import com.damei.scm.entity.prod.Catalog;

@MyBatisRepository
public interface CatalogDao extends CrudDao<Catalog> {

	List<Catalog> findSubCatalogList(@Param("parentId") Long parentId, @Param("status") StatusEnum status);

	List<Catalog> finalAllByStatus(@Param("status") StatusEnum status);

	List<Catalog> findLeafCatalogList(@Param("status") StatusEnum status);

	List<Catalog> findAll();

	Catalog getByName(Catalog catalog);
	
	void open(Catalog catalog);
	
	void lock(Catalog catalog);

	Catalog getByUrl(@Param("catalogUrl") String catalogUrl);

    List<Catalog> findCatalogByIsReject();

	Integer findCatalogParent(@Param("parentId")Long parentId);
}