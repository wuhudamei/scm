package com.mdni.scm.repository.prod;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Supplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>描述: 供应商Dao</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 下午2:07:11</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface SupplierDao extends CrudDao<Supplier> {

	/**
	 * 通过状态 商品供货商列表,返回的结果按照id升序排列
	 * 
	 * @param regionSupplierIdList 区域供应商Id列表
	 * @param status 启用/停用状态,可以为null
	 */
	List<Supplier> findSuppliersByStatusAndRegionIdsIn(@Param("regionSupplierIdList") List<Long> regionSupplierIdList,
		@Param("status") StatusEnum status);

	/**
	 * 通过代码获得供货商
	 */
	Supplier getByCode(Supplier supplier);

	Supplier getByName(Supplier supplier);

	/**
	 * 通过商品供货商id列表 查询
	 * 
	 * @param supplierIdList 供货商id列表
	 */
	List<Supplier> findByIdIn(List<Long> supplierIdList);

	/**
	 * 根据商品供应商id获取同一区域供应商下的商品供应商列表
	 * @param id
	 * @return
	 */
	List<Supplier> findRegionSupplierBySupplierId(Long id);

    List<Supplier> getByStoreCode(Map<String, Object> params);
	
	List<Map<String,Object>> findByMap(Map<String, Object> map);
}