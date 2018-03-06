package cn.damei.scm.repository.prepareorder;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.prepareorder.IndentPrepareOrderItem;

import java.util.List;

@MyBatisRepository
public interface IndentPrepareOrderItemDao extends CrudDao<IndentPrepareOrderItem> {

    List<IndentPrepareOrderItem> getByPrepareOrderId(Long prepareOrderId);
}
