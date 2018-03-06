package cn.damei.scm.repository.order;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.order.OrderItemOtherFee;

import java.util.List;

@MyBatisRepository
public interface OrderItemOtherFeeDao extends CrudDao<OrderItemOtherFee> {


    List<OrderItemOtherFee> findFeeListByItemId(Long itemId);


    List<OrderItemOtherFee> findFeeListByItemIdList(List<Long> itemIdList);
}
