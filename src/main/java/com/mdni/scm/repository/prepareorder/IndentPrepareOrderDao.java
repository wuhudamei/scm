package com.mdni.scm.repository.prepareorder;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prepareorder.IndentPrepareOrder;

/**
 * @Description: 订货预备单Dao接口
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 18:59.
 */
@MyBatisRepository
public interface IndentPrepareOrderDao extends CrudDao<IndentPrepareOrder> {

    /**
     * 通过id查询 预备单及其对应的item
     * @param id
     * @return
     */
    IndentPrepareOrder getWithItemById(Long id);
}
