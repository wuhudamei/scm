package com.mdni.scm.repository.order;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.order.OrderItemOtherFee;

import java.util.List;

/**
 * Created by 闪电侠 on 2017/8/18.
 */
@MyBatisRepository
public interface OrderItemOtherFeeDao extends CrudDao<OrderItemOtherFee> {

    /**
     * 通过itemId查询费用集合
     * @param itemId
     * @return
     */
    List<OrderItemOtherFee> findFeeListByItemId(Long itemId);

    /**
     * 通过itemId集合查询费用集合
     * @param itemIdList itemId集合
     * @return
     */
    List<OrderItemOtherFee> findFeeListByItemIdList(List<Long> itemIdList);
}
