package com.damei.scm.service.order;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.eum.AccoutTypeEnum;
import com.damei.scm.entity.eum.OrderStatusEnum;
import com.damei.scm.entity.order.OrderItem;
import com.damei.scm.repository.order.IndentOrderDao;
import com.damei.scm.repository.order.OrderItemDao;
import com.damei.scm.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OrderItemService extends CrudService<OrderItemDao, OrderItem> {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private IndentOrderDao indentOrderDao;

    public void deleteByOrderId(Long id) {
        this.orderItemDao.deleteByOrderId(id);
    }

    public List<OrderItem> findByOrderId(Long orderId, List<Long> managedSupplierIdList) {
        if (orderId == null || orderId < 1) {
            return Collections.emptyList();
        }
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if (!AccoutTypeEnum.MATERIAL_CLERK.equals(loggedUser.getAcctType())) {
            if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
                return Collections.emptyList();
            }
        }
        return entityDao.findByOrderId(orderId, managedSupplierIdList);
    }

    public List<OrderItem> getByOrderId(Long id){
        return this.orderItemDao.getByOrderId(id);
    }

   public List<OrderItem> getByOrderIdForDownload(Long id){
        return this.orderItemDao.getByOrderIdForDownload(id);
    }
    public Object updateInstallDate(OrderItem orderItem){
        return this.orderItemDao.updateInstallDate(orderItem);
    }

    public void updateStatus(OrderStatusEnum alreadyInstalled, Long id) {
        indentOrderDao.updateStatus(alreadyInstalled,id);
    }

    @Transactional
    public void updateActualTime(OrderItem orderItem) {
        entityDao.updateActualTime(orderItem);
    }
}