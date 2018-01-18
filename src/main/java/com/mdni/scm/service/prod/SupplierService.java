package com.mdni.scm.service.prod;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.PingYinUtil;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Supplier;
import com.mdni.scm.repository.prod.SupplierDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 
 * <dl>
 * <dd>描述: 供应商Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 下午4:57:58</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class SupplierService extends CrudService<SupplierDao, Supplier> {

	/**
	 * 保存/更新 商品供货商
	 */
	@Transactional
	public void saveOrUpdate(final Supplier supplier) {
		if (supplier == null) {
			return;
		}

		//1.中文拼音首字母
		supplier.setPinyinInitial(PingYinUtil.getFirstSpell(supplier.getName()));
		if (supplier.getId() != null) {
			entityDao.update(supplier);
		} else {
			supplier.setStatus(StatusEnum.OPEN);
			entityDao.insert(supplier);
		}
	}

	/**
	 * 通过状态查询供应商列表,返回的结果按照id升序排列
	 * 
	 * @param regionSupplierId 区域供应商Id
	 * @param status 启用/停用状态,可以为null
	 */
	public List<Supplier> findSuppliersByRegionIdAndStatus(Long regionSupplierId, StatusEnum status) {
		return this.findSuppliersByStatusAndRegionIdsIn(Lists.newArrayList(regionSupplierId), status);
	}

	public List<Supplier> findSuppliersByStatusAndRegionIdsIn(List<Long> regionSupplierIdList, StatusEnum status) {
		if (CollectionUtils.isEmpty(regionSupplierIdList)) {
			return Collections.emptyList();
		}

		return this.entityDao.findSuppliersByStatusAndRegionIdsIn(regionSupplierIdList, status);
	}

	/**
	 * 判断 商品代码是否已经存在
	 */
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

	/**
	 * 通过供应商id列表 查询
	 * 
	 * @param supplierIdList 供应商id列表
	 */
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