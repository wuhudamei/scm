package cn.damei.scm.repository.prod;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.prod.Store;

@MyBatisRepository
public interface StoreDao extends CrudDao<Store> {

	void batchInsert(List<Store > list);

	Store getByName(@Param("name") String name, @Param("excludeId") Long excludeRegionSupplierId);
	
	
	List<Store> findByIdIn(List<Long> storeCodeList);

	Store getByCode(String code);
}
