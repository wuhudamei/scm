package com.mdni.scm.service.orderInstallData;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.orderInstallData.OrderInstallData;
import com.mdni.scm.repository.orderInstallData.OrderInstallDao;
import org.springframework.stereotype.Service;

/**
 * Created by 刘铎 on 2017/8/28.
 */
@Service
public class OrderInstallDataService extends CrudService<OrderInstallDao,OrderInstallData>{
    public OrderInstallData getByOrderId(Long orderId) {
       OrderInstallData orderInstallData =  this.entityDao.getByOrderId(orderId);
       return orderInstallData;
    }
}
