package com.damei.scm.repository.prepareorder;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.prepareorder.IndentPrepareOrderItem;

import java.util.List;

@MyBatisRepository
public interface IndentPrepareOrderItemDao extends CrudDao<IndentPrepareOrderItem> {

    List<IndentPrepareOrderItem> getByPrepareOrderId(Long prepareOrderId);
}
