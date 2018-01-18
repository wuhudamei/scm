package com.mdni.scm.repository.prod;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prod.RegionSupplier;
import com.mdni.scm.entity.prod.Store;

@MyBatisRepository
public interface StoreDao extends CrudDao<Store> {

	/**
	 * 批量插入门店信息
	 * @param list
	 */
	void batchInsert(List<Store > list);

	/**
	 * 通过名字 获得门店
	 * 
	 * @param name 名字
	 * @param long1 
	 * @param excludeId 需要排除的区域供应商Id
	 * @param storeCode   需要排除的门店code
	 */
	Store getByName(@Param("name") String name, @Param("excludeId") Long excludeRegionSupplierId);
	
	
	/**
	 * 通过门店id列表 查询
	 * 
	 * @param storeCodeList  门店id列表
	 */
	List<Store> findByIdIn(List<Long> storeCodeList);

	/**
	 * 通过门店编码 查询 门店信息
	 * @param code 编码
	 * @return 门店信息
	 */
	Store getByCode(String code);
}
