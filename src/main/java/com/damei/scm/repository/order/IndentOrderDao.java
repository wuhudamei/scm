package com.damei.scm.repository.order;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.eum.OrderStatusEnum;
import com.damei.scm.entity.order.IndentOrder;
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