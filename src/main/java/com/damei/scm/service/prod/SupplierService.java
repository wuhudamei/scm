package com.damei.scm.service.prod;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.damei.scm.common.dto.BootstrapPage;
import com.damei.scm.common.service.CrudService;
import com.damei.scm.common.utils.PingYinUtil;
import com.damei.scm.entity.eum.StatusEnum;
import com.damei.scm.entity.prod.Supplier;
import com.damei.scm.repository.prod.SupplierDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class SupplierService extends CrudService<SupplierDao, Supplier> {

	@Transactional
	public void saveOrUpdate(final Supplier supplier) {
		if (supplier == null) {
			return;
		}

		supplier.setPinyinInitial(PingYinUtil.getFirstSpell(supplier.getName()));
		if (supplier.getId() != null) {
			entityDao.update(supplier);
		} else {
			supplier.setStatus(StatusEnum.OPEN);
			entityDao.insert(supplier);
		}
	}


	public List<Supplier> findSuppliersByRegionIdAndStatus(Long regionSupplierId, StatusEnum status) {
		return this.findSuppliersByStatusAndRegionIdsIn(Lists.newArrayList(regionSupplierId), status);
	}

	public List<Supplier> findSuppliersByStatusAndRegionIdsIn(List<Long> regionSupplierIdList, StatusEnum status) {
		if (CollectionUtils.isEmpty(regionSupplierIdList)) {
			return Collections.emptyList();
		}

		return this.entityDao.findSuppliersByStatusAndRegionIdsIn(regionSupplierIdList, status);
	}


	public boolean isCodeExist(Supplier supplier) {
		if (supplier == null || StringUtils.isBlank(supplier.getCode())) {
			return false;
		}
		return this.entityDao.getByCode(supplier) != null;
	}

	public Supplier getByName(Supplier supplier) {
		if (supplier == null || StringUtils.isBlank(supplier.getName())) {
			return null;
		}

		return this.entityDao.getByName(supplier);
	}


	public Map<Long, Supplier> findSupplierIdKeyMap(List<Long> supplierIdList) {
		if (CollectionUtils.isEmpty(supplierIdList)) {
			return MapUtils.EMPTY_MAP;
		}

		List<Supplier> supplierList = this.entityDao.findByIdIn(supplierIdList);
		if (CollectionUtils.isEmpty(supplierList)) {
			return MapUtils.EMPTY_MAP;
		}

		return Maps.uniqueIndex(supplierList, new Function<Supplier, Long>() {
			@Override
			public Long apply(Supplier input) {
				return input.getId();
			}
		});
	}

	public List<Supplier> findBySupplierId(Long supplierId){
		return this.entityDao.findRegionSupplierBySupplierId(supplierId);
	}

    public BootstrapPage<Supplier> getByStoreCode(Map<String, Object> params) {

		List<Supplier> pageData = this.entityDao.getByStoreCode(params);
		if(pageData != null && pageData.size() > 0){
			return new BootstrapPage(Long.parseLong(pageData.size()+""), pageData);
		}else {
			return new BootstrapPage(0L, Collections.EMPTY_LIST);
		}
    }
	
	public List<Map<String,Object>> findByMap(Map<String, Object> params){
		return this.entityDao.findByMap(params);
	}
}