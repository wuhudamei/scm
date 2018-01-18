package com.mdni.scm.service.order;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.order.OrderItemOtherFee;
import com.mdni.scm.repository.order.OrderItemOtherFeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 闪电侠 on 2017/8/18.
 */
/**
 * <dl>
 * <dd>描述: 其他费用Service</dd>
 * <dd>创建时间：2017/8/18 下午17:11</dd>
 * <dd>创建人： 张Paul</dd>
 * </dl>
 */
@Service
public class OrderItemOtherFeeService extends CrudService<OrderItemOtherFeeDao, OrderItemOtherFee> {

    @Autowired
    private OrderItemOtherFeeDao orderItemOtherFeeDao;

    /**
     * 通过itemId查询费用集合
     * @param itemId
     * @return
     */
    public List<OrderItemOtherFee> findFeeListByItemId(Long itemId) {
       return orderItemOtherFeeDao.findFeeListByItemId(itemId);
    }

    /**
     * 通过itemId集合查询费用集合
     * @param itemIdList itemId集合
     * @return
     */
    public List<OrderItemOtherFee> findFeeListByItemIdList(List<Long> itemIdList) {
        return orderItemOtherFeeDao.findFeeListByItemIdList(itemIdList);
    }
}
