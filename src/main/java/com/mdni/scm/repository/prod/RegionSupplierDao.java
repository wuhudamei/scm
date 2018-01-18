package com.mdni.scm.repository.prod;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.RegionSupplier;

/**
 * 
 * <dl>
 * <dd>描述:</dd>
 * <dd>公司: 区域供应商dao</dd>
 * <dd>创建时间：2017年5月12日 上午10:40:06</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface RegionSupplierDao extends CrudDao<RegionSupplier> {

	/**
	 * 通过名字 获得区域供货商
	 * 
	 * @param name 名字
	 * @param excludeId 需要排除的区域供应商Id
	 */
	RegionSupplier getByName(@Param("name") String name, @Param("excludeId") Long excludeRegionSupplierId);

	/**
	 * 通过区域供应商id列表 查询
	 * 
	 * @param regionIdList 区域供应商id列表
	 */
	List<RegionSupplier> findByIdIn(List<Long> regionIdList);

	/**
	 * 获得区域区域供应商和其包含的商品供货商
	 * 
	 * @param status 商品供货商的状态,可以为null
	 * @return
	 */
	List<RegionSupplier> findRegionWithSuppliers(@Param("status") StatusEnum status);

	/**
	 * 通过状态 区域供货商列表,返回的结果按照id升序排列
	 * 
	 * @param storeCode 门店code
	 * @param status 启用/停用状态,可以为null
	 */
	List<RegionSupplier> findRegionSuppliersByStoreCode(@Param("storeCode") String storeCode);

	List<RegionSupplier> findSameStoreIdRegionSuppliersById(Long id);

    void updateStatus(RegionSupplier regionSupplier);
}