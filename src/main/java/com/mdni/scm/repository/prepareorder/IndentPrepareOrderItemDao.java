package com.mdni.scm.repository.prepareorder;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prepareorder.IndentPrepareOrderItem;

import java.util.List;

/**
 * @Description: 订货预备单sku Dao接口
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 18:59.
 */
@MyBatisRepository
public interface IndentPrepareOrderItemDao extends CrudDao<IndentPrepareOrderItem> {
    /**
     * 根据预备单id查询预备单项
     * @param prepareOrderId 预备单id
     */
    List<IndentPrepareOrderItem> getByPrepareOrderId(Long prepareOrderId);
}
