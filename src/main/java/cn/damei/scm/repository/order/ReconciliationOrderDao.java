package cn.damei.scm.repository.order;

import java.util.Date;
import java.util.List;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.order.ReconciliationOrderItem;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface ReconciliationOrderDao extends CrudDao<ReconciliationOrderItem> {

    void updateReconciliation(@Param("ids") List<Long> ids, @Param("payTime") Date payTime, @Param("operator") Long operator);

	List<ReconciliationOrderItem> findByReconciliation(@Param("managedSupplierIdList") List<Long> managedSupplierIdList,
                                                       @Param("startTime") String startTime,
                                                       @Param("endTime") String endTime);


	List<ReconciliationOrderItem> findCheckOnWork(List<Long> ids);


    List<ReconciliationOrderItem> findByPayTime(@Param("managedSupplierIdList")List<Long> managedSupplierIdList,
                                                @Param("keyword") String keyword,
                                                @Param("startTime") String startTime,
                                                @Param("endTime") String endTime);

}
