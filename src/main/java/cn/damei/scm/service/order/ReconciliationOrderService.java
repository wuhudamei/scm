package cn.damei.scm.service.order;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.order.ReconciliationOrderItem;
import cn.damei.scm.repository.order.ReconciliationOrderDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ReconciliationOrderService extends CrudService<ReconciliationOrderDao, ReconciliationOrderItem> {
	@Autowired
	private ReconciliationOrderDao reconciliationOrderDao;

	public List<ReconciliationOrderItem> findByReconciliation(List<Long> managedSupplierIdList, String startTime, String endTime) {
		if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
			return Collections.emptyList();
		}
		return this.reconciliationOrderDao.findByReconciliation(managedSupplierIdList, startTime, endTime);
	}

	public void updateBatch(List<Long> orderItemIdList, Date payTime, Long operatorId){
		if(CollectionUtils.isEmpty(orderItemIdList)) {
			return;
		}
		this.reconciliationOrderDao.updateReconciliation(orderItemIdList,payTime, operatorId);
	}

    public List<ReconciliationOrderItem> findCheckOnWork(List<Long> orderItemIdList){
       if(orderItemIdList != null && orderItemIdList.isEmpty()){
		   return Collections.emptyList();
	   }
	   return this.reconciliationOrderDao.findCheckOnWork(orderItemIdList);
    }

	public List<ReconciliationOrderItem> findByPayTime(List<Long> managedSupplierIdList, String keyword, String startTime, String endTime) {
		if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
			return Collections.emptyList();
		}
		return this.reconciliationOrderDao.findByPayTime(managedSupplierIdList, keyword,startTime, endTime);
	}
}
