package com.mdni.scm.rest.order;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.eum.OrderStatusEnum;
import com.mdni.scm.entity.eum.PayStatusEnum;
import com.mdni.scm.entity.eum.SendStatusEnum;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.entity.order.OrderItem;
import com.mdni.scm.service.operatorLog.OperateLogService;
import com.mdni.scm.service.order.OrderItemService;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orderItem")
public class IndentOrderItemRestController extends BaseComController<OrderItemService, OrderItem> {

    @Autowired
    private OperateLogService operateLogService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) SendStatusEnum status,
                       @RequestParam(required = false) PayStatusEnum payStatus, @RequestParam(defaultValue = "0") int offset,
                       @RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "id") String orderColumn,
                       @RequestParam(defaultValue = "DESC") String orderSort) {

        ShiroUser loginUser = WebUtils.getLoggedUser();
        AccoutTypeEnum acctType1 = loginUser.getAcctType();
        if(acctType1==null){
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }

        Map<String, Object> paramMap = Maps.newHashMap();
        // keyword 意向单订单号关键词
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "status", status);
        MapUtils.putNotNull(paramMap, "payStatus", payStatus);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return service.searchScrollPage(paramMap);
    }

    // 安装/入库/通知安装
    @RequestMapping(value = "/{id}/updateStatus", method = RequestMethod.GET)
    public Object updateStatus(@PathVariable Long id ,@RequestParam(required = false) String status) {
        List<OrderItem> orderItems = this.service.getByOrderId(id);
        OperateLog operateLog = new OperateLog();
        ShiroUser user = WebUtils.getLoggedUser();
        String name = user.getName();
        if(!"storage".equals(status)){//不匹配入库
            this.service.updateStatus(OrderStatusEnum.ALREADY_INSTALLED,id);
        }
        if (orderItems == null) {
            return StatusDto.buildFailureStatusDto("订单不存在，id" + id);
        }

        for (OrderItem orderItem:orderItems) {
            if("storage".equals(status)){//匹配入库
                orderItem.setStatus(SendStatusEnum.STORAGE);
                orderItem.setStorageDate(new Date());
            }else {
                orderItem.setStatus(SendStatusEnum.ALREADY_INSTALLED);
            }
            orderItem.setEditor(WebUtils.getLoggedUser().valueOf());
            orderItem.setEditTime(new Date());
            //orderItem.setActualInstallDate(new Date());
            this.service.update(orderItem);

            operateLog.setOperator(name);
            operateLog.setOperatorTime(new Date());
            operateLog.setOrderId(orderItem.getOrderCode());
            operateLog.setContractCode(orderItem.getContractCode());
            operateLog.setOperatorExplain("对订单进行安装处理");
            operateLogService.insert(operateLog);
        }
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

    // 支付
    @RequestMapping(value = "/{id}/updatePayStatus", method = RequestMethod.GET)
    public Object updatePayStatus(@PathVariable Long id) {
        List<OrderItem> orderItems = this.service.getByOrderId(id);
        OperateLog operateLog = new OperateLog();
        ShiroUser user = WebUtils.getLoggedUser();
        String name = user.getName();
        if (orderItems == null) {
            return StatusDto.buildFailureStatusDto("订单不存在，id" + id);
        }
        for (OrderItem orderItem:orderItems) {
            orderItem.setPayStatus(PayStatusEnum.PAIED);
            orderItem.setEditor(WebUtils.getLoggedUser().valueOf());
            orderItem.setEditTime(new Date());
            //orderItem.setActualInstallDate(new Date());
            this.service.update(orderItem);

            operateLog.setOperator(name);
            operateLog.setOperatorTime(new Date());
            operateLog.setOrderId(orderItem.getOrderCode());
            operateLog.setContractCode(orderItem.getContractCode());
            operateLog.setOperatorExplain("对订单进行支付处理");
            operateLogService.insert(operateLog);
        }
        return StatusDto.buildSuccessStatusDto("支付成功！");
    }
    //填写计划安装时间
    @RequestMapping(value = "/{id}/updateInstallDate", method = RequestMethod.GET)
    public Object updateInstallDate(@PathVariable Long id,@RequestParam Date installDate){
        List<OrderItem> itemList = this.service.getByOrderId(id);
        for (OrderItem item:itemList) {
            item.setInstallDate(installDate);
            this.service.updateInstallDate(item);
        }
        return StatusDto.buildDataSuccessStatusDto("填写成功！！！");
    }
}
