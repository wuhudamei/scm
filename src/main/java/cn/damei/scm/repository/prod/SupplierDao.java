package cn.damei.scm.repository.prod;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.eum.StatusEnum;
import cn.damei.scm.entity.prod.Supplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SupplierDao extends CrudDao<Supplier> {

	List<Supplier> findSuppliersByStatusAndRegionIdsIn(@Param("regionSupplierIdList") List<Long> regionSupplierIdList,
                                                       @Param("status") StatusEnum status);

	Supplier getByCode(Supplier supplier);

	Supplier getByName(Supplier supplier);

	List<Supplier> findByIdIn(List<Long> supplierIdList);

	List<Supplier> findRegionSupplierBySupplierId(Long id);

    List<Supplier> getByStoreCode(Map<String, Object> params);
	
	List<Map<String,Object>> findByMap(Map<String, Object> map);
}