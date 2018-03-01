package com.damei.scm.repository.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.order.OrderItem;

@MyBatisRepository
public interface OrderItemDao extends CrudDao<OrderItem> {

	List<OrderItem> findByOrderId(@Param("orderId") Long orderId,
		@Param("managedSupplierIdList") List<Long> managedSupplierIdList);

	void deleteByOrderId(Long id);

	List<OrderItem> getByOrderId(Long code);

	List<OrderItem> getByOrderIdForDownload(Long code);

	int updateInstallDate(OrderItem orderItem);

    void updateActualTime(OrderItem orderItem);
}