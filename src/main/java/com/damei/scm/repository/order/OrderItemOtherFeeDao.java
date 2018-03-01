package com.damei.scm.repository.order;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.order.OrderItemOtherFee;

import java.util.List;

@MyBatisRepository
public interface OrderItemOtherFeeDao extends CrudDao<OrderItemOtherFee> {


    List<OrderItemOtherFee> findFeeListByItemId(Long itemId);


    List<OrderItemOtherFee> findFeeListByItemIdList(List<Long> itemIdList);
}
