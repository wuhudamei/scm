package com.mdni.scm.repository.prod;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prod.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * <dl>
 * <dd>描述: skuDao</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 上午10:40:06</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface SkuDao extends CrudDao<Sku> {

	List<Sku> findByIdIn(List<Long> skuIdList);

	List<Sku> findByProductId(Long productId);

	Long getSkuIdByAttributes(Map<String, Object> parameters);

	Sku getByCode(Sku sku);

	/**
	 * 增加或较少库存
	 * 
	 * @param skuId
	 * @param incrDecrement 增加量或减少量 incrDecrement>0是增量; incrDecrement<0是较少量
	 */
	void incrDecrStock(@Param("skuId") Long skuId, @Param("incrDecrement") int incrDecrement);

	/**
	 * @Ryze
	 * 审批列表 查条数
	 * @param params
	 * @return
	 */
    Long countTotal(Map<String, Object> params);
	/**
	 * @Ryze
	 * 审批列表
	 * @param params
	 * @return
	 */
	List<Sku> findApproveList(Map<String, Object> params);
	/**
	 * 已审核列表查询
	 * @param params
	 * @return
	 */

	List<Sku> findCheckList(Map<String, Object> params);


	/**
	 * @Ryze
	 * 已审批列表 查条数
	 * @param params
	 * @return
	 */
	Long checkTotal(Map<String, Object> params);
	//根据商品Id 修改Sku 状态 @Ryze
    void batchSku(Sku sku);
	
	/**
	 * 批量插入商品sku，返回商品sku集合，商品sku携带主键id值
	 * @param list
	 * @return
	 */
	List<Sku> insertBatchReturnProperty(List<Sku> list);
}