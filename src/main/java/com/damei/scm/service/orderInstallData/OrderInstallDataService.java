package com.damei.scm.service.orderInstallData;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.orderInstallData.OrderInstallData;
import com.damei.scm.repository.orderInstallData.OrderInstallDao;
import org.springframework.stereotype.Service;

@Service
public class OrderInstallDataService extends CrudService<OrderInstallDao,OrderInstallData>{
    public OrderInstallData getByOrderId(Long orderId) {
       OrderInstallData orderInstallData =  this.entityDao.getByOrderId(orderId);
       return orderInstallData;
    }
}
