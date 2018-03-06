package cn.damei.scm.service.orderInstallData;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.orderInstallData.OrderInstallData;
import cn.damei.scm.repository.orderInstallData.OrderInstallDao;
import org.springframework.stereotype.Service;

@Service
public class OrderInstallDataService extends CrudService<OrderInstallDao,OrderInstallData> {
    public OrderInstallData getByOrderId(Long orderId) {
       OrderInstallData orderInstallData =  this.entityDao.getByOrderId(orderId);
       return orderInstallData;
    }
}
