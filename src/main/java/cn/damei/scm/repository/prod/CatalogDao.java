package cn.damei.scm.repository.prod;

import java.util.List;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.eum.StatusEnum;
import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.entity.prod.Catalog;

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