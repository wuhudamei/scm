package cn.damei.scm.rest.supplierRejectRecord;

import cn.damei.scm.entity.supplierRejectRecord.SupplierRejectRecord;
import cn.damei.scm.service.account.UserService;
import cn.damei.scm.service.order.IndentOrderService;
import cn.damei.scm.service.supplierRejectRecord.SupplierRejectRecordService;
import com.google.common.collect.Maps;
import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.Constants;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.entity.account.User;
import cn.damei.scm.entity.eum.OrderStatusEnum;
import cn.damei.scm.entity.eum.ReviewSizeNoticeEnum;
import cn.damei.scm.entity.operateLog.OperateLog;
import cn.damei.scm.service.operatorLog.OperateLogService;
import cn.damei.scm.service.reviewSizeNotice.ReviewSizeNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        if ("REVIEWSIZE".equals(reject)) {
            supplierRejectRecord.setSourceType(2);
            reviewSizeNoticeService.updateReviewStatus(id, ReviewSizeNoticeEnum.REJECT);
        }else if("INSTALLCHECK".equals(reject)){
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
                supplierRejectRecord.setSourceType(1);
                indentOrderService.updateStatus(OrderStatusEnum.REJECT, id);
                operateLog.setOperator(WebUtils.getLoggedUser().getName());
                operateLog.setOperatorTime(new Date());
                operateLog.setContractCode(contractCode);
                operateLog.setOrderId(code);
                operateLog.setOperatorExplain("驳回了备货单");
                operateLogService.insert(operateLog);
            } else {
                supplierRejectRecord.setSourceType(1);
                indentOrderService.updateStatus(OrderStatusEnum.REJECTINSTALL, id);
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
