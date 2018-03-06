package cn.damei.scm.repository.order;

import java.util.List;

import cn.damei.scm.common.persistence.CrudDao;
import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.entity.order.OrderItem;

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