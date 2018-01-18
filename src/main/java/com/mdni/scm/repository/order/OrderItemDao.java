package com.mdni.scm.repository.order;

import java.util.Date;
import java.util.List;

import com.mdni.scm.entity.eum.OrderStatusEnum;
import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.order.OrderItem;

/**
 * 
 * <dl>
 * <dd>描述: 意向单Item</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月11日 上午11:06:59</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface OrderItemDao extends CrudDao<OrderItem> {

	List<OrderItem> findByOrderId(@Param("orderId") Long orderId,
		@Param("managedSupplierIdList") List<Long> managedSupplierIdList);

	void deleteByOrderId(Long id);

	List<OrderItem> getByOrderId(Long code);

	/**
	 * @Ryze 下载 订货单详情 用的
	 * @param code
	 * @return
	 */
	List<OrderItem> getByOrderIdForDownload(Long code);

	int updateInstallDate(OrderItem orderItem);
	/**
	 * 根据 orderId 修改安装时间
	 */
    void updateActualTime(OrderItem orderItem);
}