package com.damei.scm.repository.prepareorder;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.prepareorder.IndentPrepareOrder;

@MyBatisRepository
public interface IndentPrepareOrderDao extends CrudDao<IndentPrepareOrder> {


    IndentPrepareOrder getWithItemById(Long id);
}
