package com.mdni.scm.repository.order;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.order.ReconciliationOrderItem;


/**
 * <dl>
 * <dd>描述: 对账Dao</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年7月12日</dd>
 * <dd>创建人： Chaos</dd>
 * </dl>
 */
@MyBatisRepository
public interface ReconciliationOrderDao extends CrudDao<ReconciliationOrderItem> {
    /**
     * 标记对账
     */
    void updateReconciliation(@Param("ids") List<Long> ids, @Param("payTime") Date payTime, @Param("operator") Long operator);
	/**
	 * 根据供应商查询此供应商下所有未对账信息
	 * 
	 */
	List<ReconciliationOrderItem> findByReconciliation(@Param("managedSupplierIdList") List<Long> managedSupplierIdList,
                                               @Param("startTime") String startTime,
                                               @Param("endTime") String endTime);

	/**
	 * 导出
	 */
	List<ReconciliationOrderItem> findCheckOnWork(List<Long> ids);

    /**
     * 已对帐
     */
    List<ReconciliationOrderItem> findByPayTime(@Param("managedSupplierIdList")List<Long> managedSupplierIdList,
                                                @Param("keyword") String keyword,
                                                @Param("startTime") String startTime,
                                                @Param("endTime") String endTime);

}
