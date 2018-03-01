package com.damei.scm.service.prod;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.eum.StatusEnum;
import com.damei.scm.entity.prod.RegionSupplier;
import com.damei.scm.repository.prod.RegionSupplierDao;

@Service
public class RegionSupplierService extends CrudService<RegionSupplierDao, RegionSupplier> {

	public boolean checkNameExists(final RegionSupplier regioinSupplier) {
		if (regioinSupplier == null || StringUtils.isBlank(regioinSupplier.getName())) {
			return false;
		}

		return this.entityDao.getByName(regioinSupplier.getName(), regioinSupplier.getId()) != null;
	}

	public void saveOrUpdate(final RegionSupplier regionSupplier) {
		if (regionSupplier == null) {
			return;
		}

		if (regionSupplier.getId() != null) {
			entityDao.update(regionSupplier);
		} else {
			entityDao.insert(regionSupplier);
		}
	}

	public List<RegionSupplier> findRegionWithSuppliers(StatusEnum status) {
		return this.entityDao.findRegionWithSuppliers(status);
	}

	public Map<Long, RegionSupplier> findRegionSupplierIdKeyMap(List<Long> regionIdList) {
		if (CollectionUtils.isEmpty(regionIdList)) {
			return MapUtils.EMPTY_MAP;
		}

		List<RegionSupplier> supplierList = this.entityDao.findByIdIn(regionIdList);
		if (CollectionUtils.isEmpty(supplierList)) {
			return MapUtils.EMPTY_MAP;
		}

		return Maps.uniqueIndex(supplierList, new Function<RegionSupplier, Long>() {
			@Override
			public Long apply(RegionSupplier input) {
				return input.getId();
			}
		});
	}

	public List<RegionSupplier> findRegionSuppliersByStoreCode(String storeCode) {
		return this.entityDao.findRegionSuppliersByStoreCode(storeCode);
	}

	public List<RegionSupplier> findSameStoreIdRegionSuppliersById(Long regionSuppliersId){
		return this.entityDao.findSameStoreIdRegionSuppliersById(regionSuppliersId);
	}

	public void updateStatus(RegionSupplier regionSupplier) {
		this.entityDao.updateStatus(regionSupplier);
	}
}