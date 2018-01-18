package com.mdni.scm.repository.prod;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Catalog;

/**
 * 
 * <dl>
 * <dd>描述: 分类Dao</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月10日 下午2:20:30</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface CatalogDao extends CrudDao<Catalog> {

	List<Catalog> findSubCatalogList(@Param("parentId") Long parentId, @Param("status") StatusEnum status);

	List<Catalog> finalAllByStatus(@Param("status") StatusEnum status);

	List<Catalog> findLeafCatalogList(@Param("status") StatusEnum status);

	/**
	 * 查询所有分类,状态为启用
	 * 
	 * @return
	 */
	List<Catalog> findAll();

	Catalog getByName(Catalog catalog);
	
	void open(Catalog catalog);
	
	void lock(Catalog catalog);

	Catalog getByUrl(@Param("catalogUrl") String catalogUrl);

    List<Catalog> findCatalogByIsReject();

	Integer findCatalogParent(@Param("parentId")Long parentId);
}