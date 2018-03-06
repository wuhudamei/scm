package cn.damei.scm.service.order;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.order.OrderItemOtherFee;
import cn.damei.scm.repository.order.OrderItemOtherFeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemOtherFeeService extends CrudService<OrderItemOtherFeeDao, OrderItemOtherFee> {

    @Autowired
    private OrderItemOtherFeeDao orderItemOtherFeeDao;

    public List<OrderItemOtherFee> findFeeListByItemId(Long itemId) {
       return orderItemOtherFeeDao.findFeeListByItemId(itemId);
    }

    public List<OrderItemOtherFee> findFeeListByItemIdList(List<Long> itemIdList) {
        return orderItemOtherFeeDao.findFeeListByItemIdList(itemIdList);
    }
}
