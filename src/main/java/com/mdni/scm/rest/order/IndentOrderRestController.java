package com.mdni.scm.rest.order;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.PropertyHolder;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.MutipleDataStatusDto;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.common.utils.ExcelUtil;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.eum.OrderAcceptStatusEnum;
import com.mdni.scm.entity.eum.OrderStatusEnum;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.entity.order.IndentOrder;
import com.mdni.scm.entity.order.OrderItem;
import com.mdni.scm.entity.orderInstallData.OrderInstallData;
import com.mdni.scm.service.operatorLog.OperateLogService;
import com.mdni.scm.service.order.IndentOrderService;
import com.mdni.scm.service.order.OrderItemService;
import com.mdni.scm.service.orderInstallData.OrderInstallDataService;
import com.mdni.scm.service.upload.UploadService;
import com.mdni.scm.shiro.ShiroUser;
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

/**
 * <dl>
 * <dd>描述:订货单</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年6月28日 上午11:26:23</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
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


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Date endDate,
                       @RequestParam(required = false) Long brandId,
                       @RequestParam(required = false) Long id,
                       @RequestParam(required = false) String acceptStatus,
                       @RequestParam(required = false) String download,
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
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
    }

    /**
     * 订单详细
     *
     * @param orderId          订单Id
     * @param isEditDraftOrder 是否是编辑 草稿状态的订单
     */
    @RequestMapping(value = "{orderId}/detail", method = RequestMethod.GET)
    public Object getOrderDetail(@PathVariable Long orderId, @RequestParam(required = false) Boolean isEditDraftOrder) {
        // 如果是编辑订单，则获得订单的所有item信息;如果是进入订单详细页,给订单item发货，则需要根据登录账户所管理的供应商过滤数据
        List<Long> managedSupplierIds = null;
        if (isEditDraftOrder == null || Boolean.FALSE.equals(managedSupplierIds)) {
            managedSupplierIds = WebUtils.getManagedSupplierIdsOfLoginUser();
        }
        return StatusDto.buildDataSuccessStatusDto("订单详细", service.getById(orderId, true, managedSupplierIds));
    }

    /**
     * @param id 订单Id
     * @Reze 订单接收
     */
    @RequestMapping(value = "/accept", method = RequestMethod.GET)
    public Object accept(String id) {
        IndentOrder indentOrder = new IndentOrder();
        indentOrder.setId(Long.valueOf(id));
        indentOrder.setAcceptStatus(OrderAcceptStatusEnum.YES);
        indentOrder.setAcceptDate(new Date());
        service.accept(indentOrder);
        return StatusDto.buildSuccessStatusDto("修改成功");
    }


    // 通知安装
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
        //根据订单ID获取所有的订单项
        List<OrderItem> itemlist = this.orderItemService.getByOrderId(orderId);
        //遍历订单项集合，执行订单项的修改操作
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

    // 作废
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

    // 编辑或创建发货单
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Object saveOrUpdate(@RequestBody IndentOrder indentOrder) {
        if (WebUtils.getLoggedUser() == null || WebUtils.getLoggedUserId() <= 0) {
            return StatusDto.buildFailureStatusDto("未登录");
        }
        service.save(indentOrder, null);
        return StatusDto.buildSuccessStatusDto("订货单编辑成功！");
    }

    // 克隆订货单
    @RequestMapping(value = "/clone/{code}")
    public Object clone(@PathVariable Long code) {
        if (WebUtils.getLoggedUser() == null || WebUtils.getLoggedUserId() <= 0) {
            return StatusDto.buildFailureStatusDto("未登录");
        }
        this.service.clone(code);
        return StatusDto.buildSuccessStatusDto();
    }

    //重新提交通知安装后更改状态
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


    /**
     * 上传图片到服务器
     */
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

    /**
     * 从服务器删除图片
     *
     * @param path 图片路径
     * @return
     */
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
            //根据订单id修改状态为安装完成，待审核
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
            //增加复尺通知单日志
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

    /**
     * 导出
     */
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
       /* list.stream().forEach(a->{
                            a.setOrderStatus(a.getStatus().getLabel());
                            a.setOrderPayStatus(a.getPayStatus().getLabel());
                                        });*/
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
                //下载
                workbook.write(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
            //更改 下载次数统计
            IndentOrder indentOrder = new IndentOrder();
            indentOrder.setId(aLong);
            indentOrder.setDownloadDate(new Date());
            IndentOrder byId = indentOrderService.getById(aLong);
            indentOrder.setDownloadNumber(byId.getDownloadNumber() + 1);
            indentOrderService.accept(indentOrder);
        }

    }

    //初识化导出的字段
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

    /**
     * 对账列表
     *
     * @param keyword
     * @param status
     * @param offset
     * @param limit
     * @param orderColumn
     * @param orderSort
     * @return
     * @author Ryze
     */
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
        // 查询状态-->有的话查  没有默认查对账相关的
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

    /**
     * @return
     * @author Ryze
     * @date 2017-9-25 10:25:49
     * 对账操作
     */
    @RequestMapping(value = "reconciliation", method = RequestMethod.POST)
    public Object reconciliation(@RequestBody IndentOrder indentOrder) {
        return service.reconciliationOperation(indentOrder);
    }

    /**
     * @param keyword
     * @param endDate
     * @param brandId
     * @param id
     * @param acceptStatus
     * @param download
     * @param startDate
     * @param status
     * @param offset
     * @param limit
     * @param orderColumn
     * @param orderSort
     * @param contractCode
     * @return
     * @author Ryze
     * @date 2017-9-25 10:25:49
     * 获取对账列表
     * 接收参数 查询用
     */

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

    /**
     * 打印订货单详情
     *
     * @param id
     * @param res
     * @return
     */
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