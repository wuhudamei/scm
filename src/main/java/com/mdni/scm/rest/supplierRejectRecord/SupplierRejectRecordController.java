package com.mdni.scm.rest.supplierRejectRecord;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.StringUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.account.User;
import com.mdni.scm.entity.eum.OrderStatusEnum;
import com.mdni.scm.entity.eum.ReviewSizeNoticeEnum;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.entity.supplierRejectRecord.SupplierRejectRecord;
import com.mdni.scm.repository.order.IndentOrderDao;
import com.mdni.scm.service.account.UserService;
import com.mdni.scm.service.operatorLog.OperateLogService;
import com.mdni.scm.service.order.IndentOrderService;
import com.mdni.scm.service.reviewSizeNotice.ReviewSizeNoticeService;
import com.mdni.scm.service.supplierRejectRecord.SupplierRejectRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 刘铎 on 2017/8/11.
 */
@RestController
@RequestMapping("/api/supplierRejectRecord")
public class SupplierRejectRecordController extends BaseComController<SupplierRejectRecordService, SupplierRejectRecord> {

    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private IndentOrderService indentOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewSizeNoticeService reviewSizeNoticeService;


    @RequestMapping("/all")
    public Object list(@RequestParam(defaultValue = "0") int offset,
                       @RequestParam(defaultValue = "id") String orderColumn,
                       @RequestParam(defaultValue = "DESC") String orderSort,
                       @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(this.service.searchScrollPage(paramMap));
    }

    /**
     * 插入驳回信息
     *
     * @param id         订货单或复尺通知单id
     * @param rejectType 驳回类型
     * @param remark     驳回说明
     * @param reject     驳回标识码，为订货单或复尺通知单
     * @return
     */
    @RequestMapping("/updateRejectType")
    public Object updateRejectType(@RequestParam(required = false) Long id,
                                   @RequestParam(required = false) String rejectType,
                                   @RequestParam(required = false) String remark,
                                   @RequestParam(required = false) String reject,
                                   @RequestParam(required = false) String code,
                                   @RequestParam(required = false) String contractCode) {
        if (id == null) {
            return StatusDto.buildFailureStatusDto("该单号不存在！");
        }
        Long userId = WebUtils.getLoggedUser().getId();
        SupplierRejectRecord supplierRejectRecord = new SupplierRejectRecord();
        supplierRejectRecord.setSourceId(id);
        supplierRejectRecord.setRejectType(rejectType);
        if(remark == null || remark == ""){
            return StatusDto.buildFailureStatusDto("驳回原因不能为空或者前后不能有空格！！！");
        }else {
            supplierRejectRecord.setRejectReason(remark);
        }
        supplierRejectRecord.setCreator(Integer.parseInt(userId.toString()));
        OperateLog operateLog = new OperateLog();
        if ("REVIEWSIZE".equals(reject)) {//reject是标识码，用来区分是订货单还是复尺通知单（REVIEWSIZE是复尺通知单）
            supplierRejectRecord.setSourceType(2);//设置单子类型（0为备货单,1为订货单，2为复尺通知单）
            //修改复尺状态
            reviewSizeNoticeService.updateReviewStatus(id, ReviewSizeNoticeEnum.REJECT);//修改复尺通知单的状态为驳回
        }else if("INSTALLCHECK".equals(reject)){//驳回安装审核
            supplierRejectRecord.setSourceType(3);
            indentOrderService.updateStatus(OrderStatusEnum.INSTALLCHECKNOTPASS,id);
            operateLog.setOperator(WebUtils.getLoggedUser().getName());
            operateLog.setOperatorTime(new Date());
            operateLog.setContractCode(contractCode);
            operateLog.setOrderId(code);
            operateLog.setOperatorExplain("驳回了安装审核");
            operateLogService.insert(operateLog);
        }else {
            if ("DRAFT".equals(reject)) {
                supplierRejectRecord.setSourceType(1);//备货单驳回
                indentOrderService.updateStatus(OrderStatusEnum.REJECT, id);//修改备货单的状态为驳回
                operateLog.setOperator(WebUtils.getLoggedUser().getName());
                operateLog.setOperatorTime(new Date());
                operateLog.setContractCode(contractCode);
                operateLog.setOrderId(code);
                operateLog.setOperatorExplain("驳回了备货单");
                operateLogService.insert(operateLog);
            } else {
                supplierRejectRecord.setSourceType(1);//订货单驳回
                indentOrderService.updateStatus(OrderStatusEnum.REJECTINSTALL, id);//修改订货单的状态为驳回
                operateLog.setOperator(WebUtils.getLoggedUser().getName());
                operateLog.setOperatorTime(new Date());
                operateLog.setContractCode(contractCode);
                operateLog.setOrderId(code);
                operateLog.setOperatorExplain("驳回了订货单");
                operateLogService.insert(operateLog);
            }
        }
        supplierRejectRecord.setCreateTime(new Date());
        this.service.insert(supplierRejectRecord);
        return StatusDto.buildDataSuccessStatusDto("驳回成功");
    }

    /**
     * 查询驳回原因
     *
     * @param id
     * @return
     */
    @RequestMapping("/getRejectReason")
    public Object all(@RequestParam(required = false) Long id,
                      @RequestParam(required = false) Long sourceType) {
        if (id == null) {
            return StatusDto.buildFailureStatusDto("该单号不存在!");
        }
        List<SupplierRejectRecord> supplierRejectRecordList = this.service.getRejectReasonBySourceId(id, sourceType);
        for (SupplierRejectRecord supplierRejectRecord : supplierRejectRecordList) {
            User user = userService.getById(Long.valueOf(supplierRejectRecord.getCreator()));
            supplierRejectRecord.setCreateName(user.getName());
        }
        return StatusDto.buildDataSuccessStatusDto(supplierRejectRecordList);
    }


}
