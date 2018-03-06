package cn.damei.scm.repository.orderInstallData;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.orderInstallData.OrderInstallData;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface OrderInstallDao extends CrudDao<OrderInstallData>{
    OrderInstallData getByOrderId(@Param("orderId") Long orderId);
}
