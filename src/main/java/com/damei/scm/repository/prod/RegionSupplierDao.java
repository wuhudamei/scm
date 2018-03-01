package com.damei.scm.repository.prod;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.eum.StatusEnum;
import com.damei.scm.entity.prod.RegionSupplier;

@MyBatisRepository
public interface RegionSupplierDao extends CrudDao<RegionSupplier> {

	RegionSupplier getByName(@Param("name") String name, @Param("excludeId") Long excludeRegionSupplierId);

	List<RegionSupplier> findByIdIn(List<Long> regionIdList);

	List<RegionSupplier> findRegionWithSuppliers(@Param("status") StatusEnum status);

	List<RegionSupplier> findRegionSuppliersByStoreCode(@Param("storeCode") String storeCode);

	List<RegionSupplier> findSameStoreIdRegionSuppliersById(Long id);

    void updateStatus(RegionSupplier regionSupplier);
}