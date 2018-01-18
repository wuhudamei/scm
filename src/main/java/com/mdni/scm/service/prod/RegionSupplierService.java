package com.mdni.scm.service.prod;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.RegionSupplier;
import com.mdni.scm.repository.prod.RegionSupplierDao;

/**
 * 
 * <dl>
 * <dd>描述: 区域供应商Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 上午11:21:21</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class RegionSupplierService extends CrudService<RegionSupplierDao, RegionSupplier> {

	/**
	 * 校验名称是否存在
	 */
	public boolean checkNameExists(final RegionSupplier regioinSupplier) {
		if (regioinSupplier == null || StringUtils.isBlank(regioinSupplier.getName())) {
			return false;
		}

		return this.entityDao.getByName(regioinSupplier.getName(), regioinSupplier.getId()) != null;
	}

	/**
	 * 增加或者更新区域供应商
	 * 
	 * @param regionSupplier
	 */
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

	/**
	 * 获得区域区域供应商和其包含的商品供货商
	 * 
	 * @param status 商品供货商的状态,可以为null
	 * @return
	 */
	public List<RegionSupplier> findRegionWithSuppliers(StatusEnum status) {
		return this.entityDao.findRegionWithSuppliers(status);
	}

	/**
	 * 通过区域供应商id列表 查询
	 * 
	 * @param regionIdList 区域供应商id列表
	 */
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

	/**
	 * 通过状态查询区域供应商列表,返回的结果按照id升序排列
	 * 
	 * @param storeCode 门店code
	 * @param status 启用/停用状态,可以为null
	 */
	public List<RegionSupplier> findRegionSuppliersByStoreCode(String storeCode) {
		return this.entityDao.findRegionSuppliersByStoreCode(storeCode);
	}

	/**
	 * 根据区域供应商ID查询同一门店下的区域供应商列表
	 * @param regionSuppliersId
	 * @return
	 */
	public List<RegionSupplier> findSameStoreIdRegionSuppliersById(Long regionSuppliersId){
		return this.entityDao.findSameStoreIdRegionSuppliersById(regionSuppliersId);
	}

	/**
	 * 修改状态
	 * @param regionSupplier
	 */
	public void updateStatus(RegionSupplier regionSupplier) {
		this.entityDao.updateStatus(regionSupplier);
	}
}