package com.mdni.scm.repository.orderInstallData;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.orderInstallData.OrderInstallData;
import org.apache.ibatis.annotations.Param;

/**
 * Created by 刘铎 on 2017/8/28.
 */
@MyBatisRepository
public interface OrderInstallDao extends CrudDao<OrderInstallData>{
    OrderInstallData getByOrderId(@Param("orderId") Long orderId);
}
