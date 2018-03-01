package com.damei.scm.repository.orderInstallData;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.orderInstallData.OrderInstallData;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface OrderInstallDao extends CrudDao<OrderInstallData>{
    OrderInstallData getByOrderId(@Param("orderId") Long orderId);
}
