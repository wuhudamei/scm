package com.mdni.scm.repository.order;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.eum.OrderStatusEnum;
import com.mdni.scm.entity.order.IndentOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>描述: 订货单</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月11日 上午10:41:43</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface IndentOrderDao extends CrudDao<IndentOrder> {

    IndentOrder getByOrderCode(Long code);

    void updateStatus(@Param("reject") OrderStatusEnum reject, @Param("id") Long id);

    void updateIndentStatus(Long indentId);

    /**
     * @Rezy
     * 接收订单 /修改 下载次数和时间
     * @param indentOrder
     */
    void accept(IndentOrder indentOrder);

    /**
     * 根据id获取订货单详情
     * @param id
     * @return
     */
    List<IndentOrder> getOrderDetailById(@Param("id")Long id);

    /** 带条件查询
     * @param parmas
     */
    List<IndentOrder> findByQuery(Map<String, Object> parmas);
}