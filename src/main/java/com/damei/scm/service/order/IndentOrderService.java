package com.damei.scm.service.order;

import cn.mdni.commons.file.FileUtils;
import cn.mdni.commons.file.UploadCategory;
import cn.mdni.commons.pdf.PDFUtils;
import cn.mdni.commons.pdf.PdfDrawCell;
import cn.mdni.commons.pdf.PdfTablePrint;
import cn.mdni.commons.pdf.WatermarkInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPTable;
import com.damei.scm.common.Constants;
import com.damei.scm.common.PropertyHolder;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.service.CrudService;
import com.damei.scm.common.utils.CodeGenerator;
import com.damei.scm.common.utils.DateUtil;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.account.User;
import com.damei.scm.entity.eum.*;
import com.damei.scm.entity.operateLog.OperateLog;
import com.damei.scm.entity.order.IndentOrder;
import com.damei.scm.entity.order.OrderItem;
import com.damei.scm.entity.order.OrderItemOtherFee;
import com.damei.scm.entity.prod.Product;
import com.damei.scm.entity.prod.Sku;
import com.damei.scm.entity.prod.SkuPrice;
import com.damei.scm.repository.order.IndentOrderDao;
import com.damei.scm.repository.order.OrderItemDao;
import com.damei.scm.service.operatorLog.OperateLogService;
import com.damei.scm.service.prod.ProductService;
import com.damei.scm.service.prod.SkuPriceService;
import com.damei.scm.service.prod.SkuService;
import com.damei.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Collections3;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndentOrderService extends CrudService<IndentOrderDao, IndentOrder> {

    @Autowired
    private SkuService skuService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SkuPriceService priceSerice;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private IndentOrderDao indentOrderDao;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderItemOtherFeeService orderItemOtherFeeService;

    private String SQUARE_METER_TURN = "square_meter_turn";

    private String SPEC_UNIT = "片";


    public IndentOrder getById(final Long orderId, boolean isLoadItems, List<Long> managedSupplierIdList) {
        if (orderId == null || orderId < 1) {
            return null;
        }

        IndentOrder order = entityDao.getById(orderId);
        if (isLoadItems && order != null) {
            order.setOrderItemList(this.orderItemService.findByOrderId(order.getId(), managedSupplierIdList));
            //获取订货单项  并遍历 查询订货单其他费用集合
            List<OrderItem> orderItemList = order.getOrderItemList();
            if (orderItemList != null && orderItemList.size() > 0) {
                for (OrderItem item : orderItemList) {
                    item.setOtherFeesList(orderItemOtherFeeService.findFeeListByItemId(item.getId()));
                }
            }

        }
        return order;
    }


    @Transactional
    public void saveOrUpdate(IndentOrder order, String operateType) {
        IndentOrder indentOrder = this.buildOrder(order);
        if (indentOrder == null) {
            return;
        }

        ShiroUser user = WebUtils.getLoggedUser();
        String name = user.getName();
        OperateLog operateLog = new OperateLog();
        operateLog.setOperator(name);
        operateLog.setOperatorTime(new Date());
        operateLog.setContractCode(indentOrder.getContractCode());
        operateLog.setOrderId(indentOrder.getCode());


        if (indentOrder.getId() == null) {
            entityDao.insert(indentOrder);
        } else {
            entityDao.update(indentOrder);
            orderItemService.deleteByOrderId(indentOrder.getId());
        }

        //保存 订货单详细
        List<OrderItem> itemList = indentOrder.getOrderItemList();
        if (Collections3.isNotEmpty(itemList)) {
            for (OrderItem item : itemList) {
                item.setOrderId(indentOrder.getId());
                item.setStatus(SendStatusEnum.PENDING_INSTALLATION);
                item.setPayStatus(PayStatusEnum.NOT_PAIED);
                orderItemService.insert(item);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(IndentOrder order, String operateType) {
        Map<String, List<OrderItem>> orderMap = this.buildOrderItem(order);
        ShiroUser user = WebUtils.getLoggedUser();
        String name = user.getName();
        OperateLog operateLog = new OperateLog();
        for (String key : orderMap.keySet()) {
            IndentOrder indentOrder = new IndentOrder();
            if (indentOrder.getId() == null) {
                User users = WebUtils.getLoggedUser().valueOf();
                Date date = new Date();
                String branchNo = "branchNo" + date;
                indentOrder.setEditor(users);
                indentOrder.setEditTime(date);
                indentOrder.setCode(CodeGenerator.generateCode(Constants.ORDER_CODE));
                indentOrder.setContractCode(order.getContractCode());
                indentOrder.setStatus(OrderStatusEnum.DRAFT);
                indentOrder.setCreateTime(date);
                indentOrder.setCreator(users);
                indentOrder.setBranchNo(branchNo);
                if (order.getPlaceEnum() == null) {
                    indentOrder.setPlaceEnum(PlaceEnum.NORMAL);
                } else {
                    indentOrder.setPlaceEnum(order.getPlaceEnum());
                }
                operateLog.setOperator(name);
                operateLog.setOperatorTime(new Date());
                operateLog.setContractCode(indentOrder.getContractCode());
                operateLog.setOrderId(indentOrder.getCode());
                if (operateType != null) {
                    indentOrder.setPlaceEnum(PlaceEnum.CHANGE);
                    operateLog.setOperatorExplain("复制了订单");
                } else {
                    operateLog.setOperatorExplain("创建了订单");
                }
                operateLogService.insert(operateLog);
                entityDao.insert(indentOrder);
            } else {
                entityDao.update(indentOrder);
                orderItemService.deleteByOrderId(indentOrder.getId());
            }
            List<OrderItem> itemList = orderMap.get(key);
            for (OrderItem orderItem : itemList) {
                this.buildOrderItem(orderItem);
                orderItem.setOrderId(indentOrder.getId());
                orderItem.setStatus(SendStatusEnum.STAY_STORAGE);
                orderItem.setPayStatus(PayStatusEnum.NOT_PAIED);
                orderItem.setBrandId(orderItem.getSku().getProduct().getBrand().getId());
                orderItemService.insert(orderItem);

                List<OrderItemOtherFee> otherFeesList = orderItem.getOtherFeesList();
                for (OrderItemOtherFee fee : otherFeesList) {
                    if (fee.getFeeType() != null && fee.getFeeValue() != null) {
                        fee.setItemId(orderItem.getId());
                        orderItemOtherFeeService.insert(fee);
                    }
                }
            }
        }
    }

    private OrderItem buildOrderItem(OrderItem orderItem) {
        if (this.SQUARE_METER_TURN.equals(orderItem.getSku().getProduct().getCatalog().getConvertUnit())) {
            String spec = orderItem.getSku().getProduct().getSpec();
            if (spec.contains("X")) {
                spec = spec.replaceAll("X", "*");
            }
            if (spec.contains("x")) {
                spec = spec.replaceAll("x", "*");
            }
            String[] specs = spec.split("\\*");
            Double specMeter1 = new Double(specs[0]);
            Double specMetwr2 = new Double(specs[1]);
            Double specMeter = (specMeter1 * specMetwr2) / 1000000;
            BigDecimal quantity = orderItem.getQuantity();
            Double quantityDouble = quantity.doubleValue();
            Double tabletNo = quantityDouble / specMeter;
            int tabletNum = (int) Math.ceil(tabletNo);
            orderItem.setTabletNum(tabletNum);
            orderItem.setSpecUnit(this.SPEC_UNIT);
        }
        return orderItem;
    }

    public IndentOrder getByOrderCode(Long code) {
        IndentOrder indentOrder = indentOrderDao.getByOrderCode(code);
        if (indentOrder == null) {
            return new IndentOrder();
        }
        return indentOrder;
    }

    public void clone(Long code) {
        IndentOrder indentOrder = indentOrderDao.getByOrderCode(code);//获取订货单
        List<OrderItem> orderItemList = orderItemDao.getByOrderId(code);//获取商品详情
        indentOrder.setId(null);
        indentOrder.setReason(null);
        indentOrder.setOrderItemList(orderItemList);
        save(indentOrder, "复制");
    }

    public IndentOrder buildOrder(IndentOrder indentOrder) {
        User user = WebUtils.getLoggedUser().valueOf();
        Date date = new Date();
        indentOrder.setEditor(user);
        indentOrder.setEditTime(date);

        if (indentOrder.getId() == null) {
            indentOrder.setCode(CodeGenerator.generateCode(Constants.ORDER_CODE));
            indentOrder.setStatus(OrderStatusEnum.DRAFT);
            indentOrder.setCreateTime(date);
            indentOrder.setCreator(user);
        }

        for (OrderItem item : indentOrder.getOrderItemList()) {
            Sku sku = skuService.getById(item.getSku().getId());
            item.getSku().setAttribute1(sku.getAttribute1());
            item.getSku().setAttribute2(sku.getAttribute2());
            item.getSku().setAttribute3(sku.getAttribute3());
            SkuPrice curSupplyPrice = SkuPrice.calcCurrentPrice(priceSerice.findBySkuIdAndType(sku.getId(),
                    PriceTypeEnum.SUPPLY));

            if (curSupplyPrice != null) {
                item.setSupplyPrice(curSupplyPrice.getPrice());
            }

            Product product = productService.getById(item.getSku().getProduct().getId());
            if (product != null) {
                item.getSku().getProduct().setModel(product.getModel());
                item.getSku().getProduct().setSpec(product.getSpec());
                item.setSupplierId(product.getSupplier().getId());
            }
        }

        return indentOrder;
    }

    private Map<String, List<OrderItem>> buildOrderItem(IndentOrder indentOrder) {
        Map<String, List<OrderItem>> itemMap = Maps.newHashMap();
        List<OrderItem> list = indentOrder.getOrderItemList();

        for (OrderItem orderItem : list) {
            Long brandId = orderItem.getSku().getProduct().getBrand().getId();
            List<OrderItem> brandList = itemMap.get(brandId.toString());
            if (brandList == null) {
                brandList = new ArrayList<>();
            }
            Sku sku = skuService.getById(orderItem.getSku().getId());
            orderItem.getSku().setAttribute1(sku.getAttribute1());
            orderItem.getSku().setAttribute2(sku.getAttribute2());
            orderItem.getSku().setAttribute3(sku.getAttribute3());
            SkuPrice curSupplyPrice = SkuPrice.calcCurrentPrice(priceSerice.findBySkuIdAndType(sku.getId(),
                    PriceTypeEnum.SUPPLY));

            if (curSupplyPrice != null) {
                orderItem.setSupplyPrice(curSupplyPrice.getPrice());
            }

            Product product = productService.getById(orderItem.getSku().getProduct().getId());
            if (product != null) {
                orderItem.getSku().getProduct().setModel(product.getModel());
                orderItem.getSku().getProduct().setSpec(product.getSpec());
                orderItem.setSupplierId(product.getSupplier().getId());
            }
            brandList.add(orderItem);
            itemMap.put(brandId.toString(), brandList);
        }
        return itemMap;
    }

    public void updateStatus(OrderStatusEnum reject, Long indentId) {
        this.entityDao.updateStatus(reject, indentId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void accept(IndentOrder indentOrder) {
        this.entityDao.accept(indentOrder);
    }


    @Transactional(rollbackFor = Exception.class)
    public StatusDto reconciliationOperation(IndentOrder indentOrder) {
        indentOrder.setReconciliationTime(new Date());
        this.entityDao.update(indentOrder);
        //添加日志
        OperateLog operateLog = new OperateLog();
        operateLog.setOperator(WebUtils.getLoggedUser().getName());
        operateLog.setOperatorTime(new Date());
        operateLog.setContractCode(indentOrder.getContractCode());
        operateLog.setOrderId(indentOrder.getCode());
        operateLog.setOperatorExplain("对订单进行了" + indentOrder.getStatus().getLabel());
        operateLogService.insert(operateLog);
        return StatusDto.buildDataSuccessStatusDto("保存成功");
    }


    public Object printorderdetail(Long id, HttpServletRequest res) {
        String fileFullPath = getPrintPdf(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
        ResponseEntity<byte[]> responseEntity = null;
        try{
            responseEntity = new ResponseEntity<>(org.apache.commons.io.FileUtils.readFileToByteArray(new File(fileFullPath)), httpHeaders,
                    HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("打印错误!");
        }
        return responseEntity;
    }

    private String getPrintPdf(Long id){
        String tempfilePath = null;
        List<IndentOrder> list = this.indentOrderDao.getOrderDetailById(id);
        try{
            if(null != list && list.size() > 0 ){
                tempfilePath = FileUtils.saveFilePath(UploadCategory.PDF, PropertyHolder.getUploadDir(),
                        UUID.randomUUID().toString() + "." + UploadCategory.PDF.getPath());
                PdfTablePrint pdfTablePrint = new PdfTablePrint(new File(tempfilePath), PageSize.A4, 10f, 10f, 36f, 10f);
                pdfTablePrint.setFontSize(12);
                pdfTablePrint.setFontStyle(Font.BOLD);
                float[] table1Widths = {0.1F, 0.15F, 0.15F, 0.15F, 0.15F, 0.3F};
                PdfPTable table1 = pdfTablePrint.createTable(table1Widths);
                pdfTablePrint.drawTableRow("大美装饰管理平台装饰设计有限公司-订货单详情",PdfDrawCell.getTitleFont(), false);

                pdfTablePrint.drawTableRow("项目信息", Element.ALIGN_LEFT, false);
                IndentOrder order = list.get(0);
                String[] rowTwo = {"客户姓名", StringUtils.isNotBlank(order.getCustomerName())? order.getCustomerName() : "",
                            "客户电话",StringUtils.isNotBlank(order.getCustomerPhone()) ? order.getCustomerPhone() : "",
                            "装修地址", order.getHouseAddr()};
                pdfTablePrint.drawTableRowCellColSpanEmpty(rowTwo);
                String[] rowThree = {
                        "设计师", order.getDesigner(),
                        "设计师电话", order.getDesignerMobile(),
                        "监理", order.getSupervisor()
                };
                pdfTablePrint.drawTableRowCellColSpanEmpty(rowThree);
                String[] rowFour = {
                        "监理电话",order.getDesignerMobile(),
                        "项目经理", order.getProjectManager(),
                        "项目经理电话", order.getPmMobile()
                };
                pdfTablePrint.drawTableRowCellColSpanEmpty(rowFour);
                pdfTablePrint.addTable(table1);
                pdfTablePrint.drawTableRowEmpty();

                float[] relativeWidths = {0.2F, 0.08F, 0.08F, 0.08F, 0.08F, 0.08F, 0.08F, 0.08F, 0.1F, 0.1F, 0.1F, 0.08F, 0.08F, 0.08F, 0.2F};
                PdfPTable table2 = pdfTablePrint.createTable(relativeWidths);
                pdfTablePrint.drawTableRow("商品信息", Element.ALIGN_LEFT, false);
                String[] rowFive = {"商品名称", "商品型号", "商品规格", "属性值1", "属性值2", "属性值3", "订货数量", "安装位置",
                        "通知安装时间", "预计安装时间", "实际安装时间", "支付状态", "安装状态", "其他费用","备注"};
                pdfTablePrint.drawTableRow(rowFive);

                List<OrderItem> orderItemList = this.orderItemService.getByOrderId(id);

                if(null != orderItemList && orderItemList.size() > 0){

                    List<Long> itemIdList = orderItemList.stream()
                            .map(i -> i.getId())
                            .collect(Collectors.toList());

                    List<OrderItemOtherFee> AllOtherFeeList = orderItemOtherFeeService.findFeeListByItemIdList(itemIdList);
                    Map<Long, List<OrderItemOtherFee>> otherFeeMapByItemId = new HashMap<Long, List<OrderItemOtherFee>>();
                    if(AllOtherFeeList != null && AllOtherFeeList.size() > 0){
                        otherFeeMapByItemId = AllOtherFeeList.stream().filter(i -> i.getItemId() != null)
                                .collect(Collectors.groupingBy(i -> i.getItemId()));
                    }

                    for(OrderItem item : orderItemList){
                        String quantity = null;
                        String actualInstallDate = null;
                        String noticeInstallDate = null;
                        String installDate = null;
                        String payStatus = null;
                        String status = null;
                        BigDecimal otherFee = BigDecimal.ZERO;

                        if(item.getQuantity() != null){
                            if (item.getTabletNum() != null && item.getTabletNum() != 0) {
                                item.setUnitQuantity(item.getQuantity() + "m²" + "/" + item.getTabletNum() + "片");
                            } else {
                                String unit = "";
                                if (item.getSpecUnit() != null) {
                                    String[] str = item.getSpecUnit().toString().split("/");
                                    unit = str[1];
                                    item.setUnitQuantity(item.getQuantity() + unit);
                                }else {
                                    item.setUnitQuantity(item.getQuantity().toPlainString());
                                }
                            }
                            quantity = String.valueOf(item.getUnitQuantity());
                        }
                        if(item.getActualInstallDate() != null){
                            actualInstallDate = DateUtil.formatDate(item.getActualInstallDate());
                        }
                        if(item.getNoticeInstallDate() != null){
                            noticeInstallDate = DateUtil.formatDate(item.getNoticeInstallDate());
                        }
                        if(item.getInstallDate() != null){
                            installDate = DateUtil.formatDate(item.getInstallDate());
                        }
                        if(item.getPayStatus() != null){
                            payStatus = item.getPayStatus().toString();
                            if(payStatus.equals("NOT_PAIED")){
                                payStatus = "待结算";
                            }else{
                                payStatus = "已结算";
                            }
                        }
                        if(item.getStatus() != null){
                            status = item.getStatus().toString();
                            if(status.equals("PENDING_INSTALLATION")){
                                status = "待安装";
                            }else if(status.equals("ALREADY_INSTALLED")){
                                status = "已安装";
                            }else if(status.equals("STAY_STORAGE")){
                                status = "待入库";
                            }else if(status.equals("STORAGE")){
                                status = "已入库";
                            }

                        }
                        if(item.getId() != null || item.getId() != 0){
                            if(otherFeeMapByItemId.size() > 0 ){
                                List<OrderItemOtherFee> otherFeeListByItemId = otherFeeMapByItemId.get(item.getId());
                                if(otherFeeListByItemId != null && otherFeeListByItemId.size() > 0){
                                    for(OrderItemOtherFee otherFees : otherFeeListByItemId){
                                        if(otherFees.getFeeValue() != null){
                                            otherFee = otherFee.add(otherFees.getFeeValue());
                                        }
                                    }
                                }
                            }
                        }

                        String[] rowSix = {item.getSku().getName(), item.getSku().getProduct().getModel(),
                                item.getSku().getProduct().getSpec(), item.getSku().getAttribute1(),
                                item.getSku().getAttribute2(), item.getSku().getAttribute3(),
                                StringUtils.isNotBlank(quantity) ? quantity:"",
                                item.getInstallationLocation(), StringUtils.isNotBlank(noticeInstallDate) ? noticeInstallDate : "",
                                StringUtils.isNotBlank(installDate) ? installDate: "",
                                StringUtils.isNotBlank(actualInstallDate) ? actualInstallDate : "",
                                StringUtils.isNotBlank(payStatus) ? payStatus : "",
                                StringUtils.isNotBlank(status) ?  status : "",
                                otherFee.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                                item.getNote()};
                        pdfTablePrint.drawTableRowCellColSpanEmpty(rowSix);
                    }
                }
                pdfTablePrint.addTable(table2);
                pdfTablePrint.build(true);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("创建pdf失败! " + e);
        }
        return tempfilePath;
    }

    public String addWater(String imgerFullFile,String souceFilePath) {
        UploadCategory uploadCategory = UploadCategory.PDF;
        String basePath = PropertyHolder.getBaseurl();
        String fileName = UUID.randomUUID().toString()+".pdf";
        String fileFullPath = FileUtils.saveFilePath(uploadCategory, basePath, fileName);
        List<WatermarkInfo> watermarkInfoList = Lists.newArrayList();
        watermarkInfoList.add(new WatermarkInfo("大美装饰管理平台", 60, 620, 1, 0.1f));
        watermarkInfoList.add(new WatermarkInfo("大美装饰管理平台", 360, 480, 1, 0.1f));
        watermarkInfoList.add(new WatermarkInfo("大美装饰管理平台", 60, 280, 1, 0.1f));
        watermarkInfoList.add(new WatermarkInfo(imgerFullFile , 40, 760, 0, 1f));
        try {
            PDFUtils.addWatermark(souceFilePath,fileFullPath, watermarkInfoList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return fileFullPath;
    }


}