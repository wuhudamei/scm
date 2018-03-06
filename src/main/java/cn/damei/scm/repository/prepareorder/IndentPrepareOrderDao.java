package cn.damei.scm.repository.prepareorder;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.prepareorder.IndentPrepareOrder;
import cn.damei.scm.common.MyBatisRepository;

@MyBatisRepository
public interface IndentPrepareOrderDao extends CrudDao<IndentPrepareOrder> {


    IndentPrepareOrder getWithItemById(Long id);
}
