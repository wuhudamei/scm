package cn.damei.scm.rest.order;

import cn.damei.scm.common.Constants;
import cn.damei.scm.common.dto.MutipleDataStatusDto;
import cn.damei.scm.common.utils.ExcelUtil;
import cn.damei.scm.entity.eum.AccoutTypeEnum;
import cn.damei.scm.entity.eum.OrderAcceptStatusEnum;
import cn.damei.scm.entity.eum.OrderStatusEnum;
import cn.damei.scm.entity.order.IndentOrder;
import cn.damei.scm.entity.order.OrderItem;
import cn.damei.scm.service.operatorLog.OperateLogService;
import cn.damei.scm.service.order.IndentOrderService;
import cn.damei.scm.service.upload.UploadService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.PropertyHolder;
import cn.damei.scm.common.dto.BootstrapPage;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.DateUtil;
import cn.damei.scm.common.utils.MapUtils;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.entity.operateLog.OperateLog;
import cn.damei.scm.entity.orderInstallData.OrderInstallData;
import cn.damei.scm.service.order.OrderItemService;
import cn.damei.scm.service.orderInstallData.OrderInstallDataService;
import cn.damei.scm.shiro.ShiroUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "/api/order")
public class IndentOrderRestController extends BaseComController<IndentOrderService, IndentOrder> {


    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private IndentOrderService indentOrderService;
    @Autowired
    private OrderInstallDataService orderInstallDataService;
    private static final String CREATE_DATE = "CREATE_DATE";

    private static final String ACCEPT_DATE = "ACCEPT_DATE";

    private static final String DOWNLOAD_DATE = "DOWNLOAD_DATE";
    private static final String NOTICE_INSTALL_TIME = "NOTICE_INSTALL_TIME";


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Date endDate,
                       @RequestParam(required = false) Long brandId,
                       @RequestParam(required = false) Long id,
                       @RequestParam(required = false) String acceptStatus,
                       @RequestParam(required = false) String download,
                       @RequestParam(required = false)String dateType,
                       @RequestParam(required = false) Date startDate, @RequestParam(required = false) OrderStatusEnum status,
                       @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit,
                       @RequestParam(defaultValue = "id") String orderColumn, @RequestParam(defaultValue = "DESC") String orderSort,
                       @RequestParam(required = false) String contractCode) {


        ShiroUser loginUser = WebUtils.getLoggedUser();
        AccoutTypeEnum acctType1 = loginUser.getAcctType();
        if (acctType1 == null) {
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }

        List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();
        if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "brandId", brandId);
        MapUtils.putNotNull(paramMap, "acceptStatus", acceptStatus);
        MapUtils.putNotNull(paramMap, "id", id);
        MapUtils.putNotNull(paramMap, "download", download);
        MapUtils.putNotNull(paramMap, "startDate", startDate);
        MapUtils.putNotNull(paramMap, "endDate", endDate);
        MapUtils.putNotNull(paramMap, "status", status);
        MapUtils.putNotNull(paramMap, "managedSupplierIdList", managedSupplierIdList);
        //时间的勾选类型
        if (CREATE_DATE.equals(dateType)) {
            paramMap.put("createStartDate", startDate);
            paramMap.put("createEndDate", endDate);
        } else if (ACCEPT_DATE.equals(dateType)) {
            paramMap.put("acceptStartDate", startDate);
            paramMap.put("acceptEndDate", endDate);
        } else if (DOWNLOAD_DATE.equals(dateType)) {
            paramMap.put("downloadStartDate", startDate);
            paramMap.put("downloadEndDate", endDate);
        } else if (NOTICE_INSTALL_TIME.equals(dateType)) {
            paramMap.put("noticeInstallStartDate", startDate);
            paramMap.put("noticeInstallEndDate", endDate);
        }
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
    }

    @RequestMapping(value = "{orderId}/detail", method = RequestMethod.GET)
    public Object getOrderDetail(@PathVariable Long orderId, @RequestParam(required = false) Boolean isEditDraftOrder) {
        List<Long> managedSupplierIds = null;
        if (isEditDraftOrder == null || Boolean.FALSE.equals(managedSupplierIds)) {
            managedSupplierIds = WebUtils.getManagedSupplierIdsOfLoginUser();
        }
        return StatusDto.buildDataSuccessStatusDto("订单详细", service.getById(orderId, true, managedSupplierIds));
    }

    @RequestMapping(value = "/accept", method = RequestMethod.GET)
    public Object accept(String id) {
        IndentOrder indentOrder = new IndentOrder();
        indentOrder.setId(Long.valueOf(id));
        indentOrder.setAcceptStatus(OrderAcceptStatusEnum.YES);
        indentOrder.setAcceptDate(new Date());
        service.accept(indentOrder);
        return StatusDto.buildSuccessStatusDto("修改成功");
    }


    @RequestMapping(value = "/{orderId}/notify", method = RequestMethod.GET)
    public Object notify(@PathVariable Long orderId) {
        IndentOrder oldOrder = this.service.getById(orderId);
        if (oldOrder == null) {
            return StatusDto.buildFailureStatusDto("订单不存在,orderId:" + orderId);
        }

        if (!OrderStatusEnum.DRAFT.equals(oldOrder.getStatus())) {
            return StatusDto.buildFailureStatusDto("只有草稿状态才可以通知供货商");
        }
        IndentOrder order = new IndentOrder();
        order.setId(orderId);
        order.setStatus(OrderStatusEnum.NOTIFIED);
        order.setNoticeInstallTime(new Date());
        this.service.update(order);
        List<OrderItem> itemlist = this.orderItemService.getByOrderId(orderId);
        for (OrderItem item : itemlist) {
            item.setNoticeInstallDate(new Date());
            this.orderItemService.update(item);
        }

        ShiroUser user = WebUtils.getLoggedUser();
        String name = user.getName();
        OperateLog operateLog = new OperateLog();
        operateLog.setOperator(name);
        operateLog.setOperatorTime(new Date());
        operateLog.setContractCode(oldOrder.getContractCode());
        operateLog.setOrderId(oldOrder.getCode());
        operateLog.setOperatorExplain("对订单进行了通知安装处理");
        operateLogService.insert(operateLog);
        return StatusDto.buildSuccessStatusDto("订单通知安装成功！");

    }

    @RequestMapping(value = "/cancle", method = RequestMethod.POST)
    public Object cancle(@RequestParam Long orderId, @RequestParam String reason) {
        IndentOrder oldOrder = this.service.getById(orderId);
        if (oldOrder == null) {
            return StatusDto.buildFailureStatusDto("订单不存在,orderId:" + orderId);
        }
        if (oldOrder.getStatus().equals(OrderStatusEnum.INVALID)) {
            return StatusDto.buildFailureStatusDto("草稿单或已通知发货单 才允许作废");
        }
        if (StringUtils.isBlank(reason)) {
            return StatusDto.buildFailureStatusDto("作废原因不可为空");
        }
        IndentOrder order = new IndentOrder();
        order.setId(orderId);
        order.setStatus(OrderStatusEnum.INVALID);
        order.setReason(reason);
        this.service.update(order);

        ShiroUser user = WebUtils.getLoggedUser();
        String name = user.getName();
        OperateLog operateLog = new OperateLog();
        operateLog.setOperator(name);
        operateLog.setOperatorTime(new Date());
        operateLog.setContractCode(oldOrder.getContractCode());
        operateLog.setOrderId(oldOrder.getCode());
        operateLog.setOperatorExplain("对订单进行了作废处理");
        operateLogService.insert(operateLog);

        return StatusDto.buildSuccessStatusDto("订单作废操作成功！");

    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Object saveOrUpdate(@RequestBody IndentOrder indentOrder) {
        if (WebUtils.getLoggedUser() == null || WebUtils.getLoggedUserId() <= 0) {
            return StatusDto.buildFailureStatusDto("未登录");
        }
        service.save(indentOrder, null);
        return StatusDto.buildSuccessStatusDto("订货单编辑成功！");
    }

    @RequestMapping(value = "/clone/{code}")
    public Object clone(@PathVariable Long code) {
        if (WebUtils.getLoggedUser() == null || WebUtils.getLoggedUserId() <= 0) {
            return StatusDto.buildFailureStatusDto("未登录");
        }
        this.service.clone(code);
        return StatusDto.buildSuccessStatusDto();
    }

    @RequestMapping("/updateStatus")
    public Object updateStatus(@RequestParam Long indentId,
                               @RequestParam(required = false) String code,
                               @RequestParam(required = false) String contractCode) {
        if (indentId == null) {
            return StatusDto.buildFailureStatusDto("订货单不存在");
        }
        IndentOrder order = this.service.getById(indentId);
        order.setStatus(OrderStatusEnum.NOTIFIED);
        order.setNoticeInstallTime(new Date());
        this.service.update(order);
        OperateLog operateLog = new OperateLog();
        operateLog.setOperator(WebUtils.getLoggedUser().getName());
        operateLog.setOperatorTime(new Date());
        operateLog.setContractCode(contractCode);
        operateLog.setOrderId(code);
        operateLog.setOperatorExplain("重新提交了通知安装");
        operateLogService.insert(operateLog);
        return StatusDto.buildDataSuccessStatusDto("更改成功");
    }


    @RequestMapping(method = RequestMethod.POST)
    public Object upload(HttpServletRequest req, HttpServletResponse response, MultipartFile file,
                         @RequestParam UploadService.UploadCategory type) {

        String saveTmpPath = StringUtils.EMPTY;
        try {
            saveTmpPath = uploadService.upload(file, type);
        } catch (Exception e) {
            return StatusDto.buildFailureStatusDto(e.getMessage());
        }

        String imgPreviewPath = PropertyHolder.getFullImageUrl(saveTmpPath);
        MutipleDataStatusDto dto = MutipleDataStatusDto.buildMutipleDataSuccessDto();
        dto.append("fullPath", imgPreviewPath).append("path", saveTmpPath);
        return dto;

    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Object delete(@RequestParam String path) {
        uploadService.delete(path);
        return StatusDto.buildSuccessStatusDto();
    }

    @RequestMapping("/orderInstallData")
    public Object installByMaterialClerkReject(@RequestBody List<OrderInstallData> orderInstallDataList) {
        if (orderInstallDataList == null) {
            return StatusDto.buildFailureStatusDto("");
        }
        String name = WebUtils.getLoggedUser().getName();
        Date date = new Date();
        for (OrderInstallData orderInstallData : orderInstallDataList) {
            orderInstallData.setCreator(name);
            orderInstallData.setCreatTime(date);
            OrderItem orderItem = new OrderItem();
            orderItem.setActualInstallDate(new Date());
            orderItem.setOrderId(orderInstallData.getOrderId());
            orderItemService.updateActualTime(orderItem);
            IndentOrder indentOrder = new IndentOrder();
            indentOrder.setId(orderInstallData.getOrderId());
            indentOrder.setStatus(OrderStatusEnum.INSTALLEND_WAITCHECK);
            indentOrder.setActualInstallationTime(new Date());
            indentOrderService.update(indentOrder);
            orderInstallDataService.insert(orderInstallData);
            OperateLog operateLog = new OperateLog();
            operateLog.setOperator(WebUtils.getLoggedUser().getName());
            operateLog.setOperatorTime(new Date());
            operateLog.setOperatorExplain("提交了安装审核");
            operateLog.setOrderId(orderInstallData.getOrderCode());
            operateLog.setContractCode(orderInstallData.getContractCode());
            operateLogService.insert(operateLog);
        }
        return StatusDto.buildSuccessStatusDto("操作成功");

    }
    @RequestMapping("/export")
    public void export(String id, HttpServletResponse resp) {
        ServletOutputStream out = null;
        Workbook workbook = null;
        Long aLong = Long.valueOf(id);
        List<OrderItem> list = orderItemService.getByOrderIdForDownload(aLong);
        for (OrderItem orderItem : list) {
            if (orderItem.getTabletNum() != null && orderItem.getTabletNum() != 0) {
                orderItem.setUnitQuantity(orderItem.getQuantity() + "m²" + "/" + orderItem.getTabletNum() + "片");
            } else {
                String unit = "";
                if (orderItem.getSpecUnit() != null) {
                    String[] str = orderItem.getSpecUnit().toString().split("/");
                    unit = str[1];
                    orderItem.setUnitQuantity(orderItem.getQuantity() + unit);
                }else {
                    orderItem.setUnitQuantity(orderItem.getQuantity().toPlainString());
                }
            }
            orderItem.setOrderStatus(orderItem.getStatus().getLabel());
            orderItem.setOrderPayStatus(orderItem.getPayStatus().getLabel());
        }
        try {
            resp.setContentType("application/x-msdownload");
            Date date = new Date();
            String strDate = DateUtil.formatDate(date, "yyyyMMddHHmmss");
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(strDate + "订货单详细信息.xlsx", "UTF-8") + "\"");
            out = resp.getOutputStream();
            if (CollectionUtils.isNotEmpty(list)) {
                workbook = ExcelUtil.getInstance().exportObj2ExcelWithTitleAndFields(list, OrderItem.class, true, titles, fields);
            }
            if (workbook != null) {
                workbook.write(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
            IndentOrder indentOrder = new IndentOrder();
            indentOrder.setId(aLong);
            indentOrder.setDownloadDate(new Date());
            IndentOrder byId = indentOrderService.getById(aLong);
            indentOrder.setDownloadNumber(byId.getDownloadNumber() + 1);
            indentOrderService.accept(indentOrder);
        }

    }

    private static List<String> titles = null;
    private static List<String> fields = null;

    static {
        titles = Lists.newArrayListWithExpectedSize(3);
        titles.add("客户姓名");
        titles.add("客户电话");
        titles.add("装修地址");
        titles.add("设计师");
        titles.add("设计师电话");
        titles.add("监理");
        titles.add("监理电话");
        titles.add("项目经理");
        titles.add("项目经理电话");
        titles.add("项目编号");
        titles.add("商品名称");
        titles.add("商品型号");
        titles.add("商品规格");
        titles.add("属性值1");
        titles.add("属性值2");
        titles.add("属性值3");
        titles.add("订货数量");
        titles.add("安装位置");
        titles.add("通知安装时间");
        titles.add("预计安装时间");
        titles.add("实际安装时间");
        titles.add("支付状态");
        titles.add("安装状态");
        titles.add("其他费用");
        titles.add("备注");
        fields = Lists.newArrayListWithExpectedSize(3);

        fields.add("name");
        fields.add("mobile");
        fields.add("houseAddr");
        fields.add("designer");
        fields.add("designerMobile");
        fields.add("supervisor");
        fields.add("supervisorMobile");
        fields.add("projectManager");
        fields.add("pmMobile");
        fields.add("contractCode");
        fields.add("sku.name");
        fields.add("sku.product.model");
        fields.add("sku.product.spec");
        fields.add("sku.attribute1");
        fields.add("sku.attribute2");
        fields.add("sku.attribute3");
        fields.add("unitQuantity");
        fields.add("installationLocation");
        fields.add("noticeInstallDate");
        fields.add("installDate");
        fields.add("actualInstallDate");
        fields.add("orderPayStatus");
        fields.add("orderStatus");
        fields.add("otherFee");
        fields.add("note");

    }

    @RequestMapping(value = "reconciliationList", method = RequestMethod.GET)
    public Object reconciliationList(@RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) OrderStatusEnum status,
                                     @RequestParam(defaultValue = "0") int offset,
                                     @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(defaultValue = "id") String orderColumn,
                                     @RequestParam(defaultValue = "DESC") String orderSort
    ) {

        ShiroUser loginUser = WebUtils.getLoggedUser();
        AccoutTypeEnum acctType1 = loginUser.getAcctType();
        if (acctType1 == null) {
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }

        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        ArrayList<OrderStatusEnum> str = new ArrayList<OrderStatusEnum>();
        if (status != null) {
            str.add(status);
        } else {
            str.add(OrderStatusEnum.NOTRECONCILED);
            str.add(OrderStatusEnum.HASBEENRECONCILED);
            str.add(OrderStatusEnum.PARTIALRECONCILIATION);
        }
        MapUtils.putNotNull(paramMap, "statusList", str);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
    }

    @RequestMapping(value = "reconciliation", method = RequestMethod.POST)
    public Object reconciliation(@RequestBody IndentOrder indentOrder) {
        return service.reconciliationOperation(indentOrder);
    }


    @RequestMapping(value = "listReconciliation", method = RequestMethod.GET)
    public Object listReconciliation(@RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) Date endDate,
                                     @RequestParam(required = false) Long brandId,
                                     @RequestParam(required = false) Long id,
                                     @RequestParam(required = false) String acceptStatus,
                                     @RequestParam(required = false) String download,
                                     @RequestParam(required = false) Date startDate, @RequestParam(required = false) OrderStatusEnum status,
                                     @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(defaultValue = "id") String orderColumn, @RequestParam(defaultValue = "DESC") String orderSort,
                                     @RequestParam(required = false) String contractCode) {
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "brandId", brandId);
        MapUtils.putNotNull(paramMap, "acceptStatus", acceptStatus);
        MapUtils.putNotNull(paramMap, "id", id);
        MapUtils.putNotNull(paramMap, "download", download);
        MapUtils.putNotNull(paramMap, "startDate", startDate);
        MapUtils.putNotNull(paramMap, "endDate", endDate);
        MapUtils.putNotNull(paramMap, "status", status);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
    }

    @RequestMapping("/printorderdetail")
    public Object printOrderDetail(@RequestParam String id, HttpServletRequest res) {
        Long valueOf = Long.valueOf(id);
        if (null == valueOf || valueOf == 0) {
            return StatusDto.buildFailureStatusDto("参数不能为空!");
        } else {
            return this.indentOrderService.printorderdetail(valueOf, res);
        }
    }

}