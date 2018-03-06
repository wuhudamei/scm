package cn.damei.scm.repository.order;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.eum.OrderStatusEnum;
import cn.damei.scm.entity.order.IndentOrder;
import cn.damei.scm.common.MyBatisRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface IndentOrderDao extends CrudDao<IndentOrder> {

    IndentOrder getByOrderCode(Long code);

    void updateStatus(@Param("reject") OrderStatusEnum reject, @Param("id") Long id);

    void updateIndentStatus(Long indentId);

    void accept(IndentOrder indentOrder);


    List<IndentOrder> getOrderDetailById(@Param("id")Long id);


    List<IndentOrder> findByQuery(Map<String, Object> parmas);
}