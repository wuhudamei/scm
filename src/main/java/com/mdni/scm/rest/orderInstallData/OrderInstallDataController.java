package com.mdni.scm.rest.orderInstallData;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.OrderStatusEnum;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.entity.orderInstallData.OrderInstallData;
import com.mdni.scm.service.operatorLog.OperateLogService;
import com.mdni.scm.service.order.IndentOrderService;
import com.mdni.scm.service.orderInstallData.OrderInstallDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by 刘铎 on 2017/8/28.
 */
@RestController
@RequestMapping("/api/orderInstallData")
public class OrderInstallDataController extends BaseComController<OrderInstallDataService,OrderInstallData>{

    @Autowired
    private IndentOrderService indentOrderService;
    @Autowired
    private OperateLogService operateLogService;

    @RequestMapping("/getByOrderId")
    public Object getByOrderId(@RequestParam(required = true) Long orderId){
        if(orderId == null){
            return StatusDto.buildFailureStatusDto("该单号不存在");
        }
       OrderInstallData orderInstallData =  this.service.getByOrderId(orderId);
        return StatusDto.buildDataSuccessStatusDto(orderInstallData);
    }

    @RequestMapping("/updateOrderStatus")
    public Object updateOrderStatus(@RequestParam(required = true) Long orderId,
                                    @RequestParam(required = false) String orderCode,
                                    @RequestParam(required = false) String contractcode){
        if(orderId == null){
            return StatusDto.buildFailureStatusDto("该单号不存在");
        }
        indentOrderService.updateStatus(OrderStatusEnum.NOTRECONCILED,orderId);
        OperateLog operateLog = new OperateLog();
        operateLog.setContractCode(contractcode);
        operateLog.setOperatorExplain("通过了安装审核");
        operateLog.setOperator(WebUtils.getLoggedUser().getName());
        operateLog.setOperatorTime(new Date());
        operateLog.setOrderId(orderCode);
        operateLogService.insert(operateLog);
        return StatusDto.buildDataSuccessStatusDto("操作成功");
    }

}
