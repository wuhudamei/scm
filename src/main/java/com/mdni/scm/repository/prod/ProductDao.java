package com.mdni.scm.repository.prod;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Product;

/**
 * <dl>
 * <dd>描述: 商品Dao</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月15日 下午1:31:08</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface ProductDao extends CrudDao<Product> {

	List<Product> adminSearch(Map<String, Object> params);

	Long adminSearchTotal(Map<String, Object> params);

	List<String> findAllCode();

	/**
	 * 通过商品代码获得商品
	 * 
	 * @param code 商品代码
	 */
	Product getByCode(Product product);

	/**
	 * 通过id列表获得商品
	 * 
	 * @param productIdList 产品列表
	 * @param status 商品状态，可以为null,null则表示返回所有状态
	 */
	List<Product> findByIdInList(@Param("ids") List<Long> productIdList, @Param("status") StatusEnum status);

	/**
	 * 返回排序后的 商品位置列表,返回的结果按照position或分类序 升序排列
	 * 
	 * @param categoryUrl 分类url,可以为null,如果为null,则按照全局序排列，否则按照分类序排列
	 */
	List<Product> findOrderedPositionList(@Param("categoryUrl") String categoryUrl);

	Product getByInfoById(Long id);
	
	/**
	 * 根据map参数查询商品
	 * @param map
	 * productName	商品名称
	 * brandName	品牌名称
	 * supplierName	供应商名称
	 * @return
	 */
	Product getByMap(Map<String,String> map);
	
	/**
	 * 批量插入商品，返回商品集合，商品携带主键id值
	 * @param list
	 * @return
	 */
	List<Product> insertBatchReturnProperty(List<Product> list);
}