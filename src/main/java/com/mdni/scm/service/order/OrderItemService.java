package com.mdni.scm.service.order;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.eum.OrderStatusEnum;
import com.mdni.scm.entity.order.OrderItem;
import com.mdni.scm.repository.order.IndentOrderDao;
import com.mdni.scm.repository.order.OrderItemDao;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <dl>
 * <dd>描述:订货单详细</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月11日 下午1:53:28</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class OrderItemService extends CrudService<OrderItemDao, OrderItem> {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private IndentOrderDao indentOrderDao;

    public void deleteByOrderId(Long id) {
        this.orderItemDao.deleteByOrderId(id);
    }

    /**
     * @param managedSupplierIdList 能够管理的供应商列表
     * @param orderId               订单Id
     * @return 通过orderId获得订单Item列表, 如果没有获取到则返回空列表
     */
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

    /**
     * 根据订单ID，查询所有此订单下的订单项
     * @param id 订单ID
     * @return 返回所有此订单下的订单项
     */
    public List<OrderItem> getByOrderId(Long id){
        return this.orderItemDao.getByOrderId(id);
    }
    /**
     * @Ryze 下载 订货单详情 用的
     * @param id
     * @return
     */
   public List<OrderItem> getByOrderIdForDownload(Long id){
        return this.orderItemDao.getByOrderIdForDownload(id);
    }
    public Object updateInstallDate(OrderItem orderItem){
        return this.orderItemDao.updateInstallDate(orderItem);
    }

    public void updateStatus(OrderStatusEnum alreadyInstalled, Long id) {
        indentOrderDao.updateStatus(alreadyInstalled,id);
    }

    /**
     * 根据 orderId 修改安装时间
     */
    @Transactional
    public void updateActualTime(OrderItem orderItem) {
        entityDao.updateActualTime(orderItem);
    }
}