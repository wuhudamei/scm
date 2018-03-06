package cn.damei.scm.repository.prod;

import java.util.List;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.eum.StatusEnum;
import cn.damei.scm.entity.prod.RegionSupplier;
import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;

@MyBatisRepository
public interface RegionSupplierDao extends CrudDao<RegionSupplier> {

	RegionSupplier getByName(@Param("name") String name, @Param("excludeId") Long excludeRegionSupplierId);

	List<RegionSupplier> findByIdIn(List<Long> regionIdList);

	List<RegionSupplier> findRegionWithSuppliers(@Param("status") StatusEnum status);

	List<RegionSupplier> findRegionSuppliersByStoreCode(@Param("storeCode") String storeCode);

	List<RegionSupplier> findSameStoreIdRegionSuppliersById(Long id);

    void updateStatus(RegionSupplier regionSupplier);
}