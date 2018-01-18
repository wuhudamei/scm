package com.mdni.scm.service.order;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.order.ReconciliationOrderItem;
import com.mdni.scm.repository.order.ReconciliationOrderDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <dl>
 * <dd>描述: 对账Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年7月12日</dd>
 * <dd>创建人： Chaos</dd>
 * </dl>
 */
@Service
public class ReconciliationOrderService extends CrudService<ReconciliationOrderDao, ReconciliationOrderItem> {
	@Autowired
	private ReconciliationOrderDao reconciliationOrderDao;

	/**
	 * 根据供应商查询此供应商下所有未对账信息
	 * @param startTime 查询参数，发货时间大于的时间
	 * @param endTime 查询参数，发货时间小于的时间
	 * @param managedSupplierIdList 查询参数，供应商id集合
	 * @return 发货时间区间内，某个供应商所对应的商品未对账列表
	 */
	public List<ReconciliationOrderItem> findByReconciliation(List<Long> managedSupplierIdList, String startTime, String endTime) {
		if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
			return Collections.emptyList();
		}
		return this.reconciliationOrderDao.findByReconciliation(managedSupplierIdList, startTime, endTime);
	}

	/**
	 * 批量修改
	 * @param orderItemIdList 根据订单item表的主键去修改
	 * @param payTime 操作时间
	 * @param operatorId 操作人
	 */
	public void updateBatch(List<Long> orderItemIdList, Date payTime, Long operatorId){
		if(CollectionUtils.isEmpty(orderItemIdList)) {
			return;
		}
		this.reconciliationOrderDao.updateReconciliation(orderItemIdList,payTime, operatorId);
	}

    /**
     * 导出
     */
    public List<ReconciliationOrderItem> findCheckOnWork(List<Long> orderItemIdList){
       if(orderItemIdList != null && orderItemIdList.isEmpty()){
		   return Collections.emptyList();
	   }
	   return this.reconciliationOrderDao.findCheckOnWork(orderItemIdList);
    }

	/**
	 * 已对账
	 * @param managedSupplierIdList 查询参数，供应商id集合
	 * @param startTime 查询参数，对账时间大于的时间
	 * @param endTime 查询参数，对账时间小于的时间
	 * @return 对账时间所在时间区间内的已对账信息列表
	 */
	public List<ReconciliationOrderItem> findByPayTime(List<Long> managedSupplierIdList, String keyword, String startTime, String endTime) {
		if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
			return Collections.emptyList();
		}
		return this.reconciliationOrderDao.findByPayTime(managedSupplierIdList, keyword,startTime, endTime);
	}
}
