package com.mdni.scm.rest.prod;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Product;
import com.mdni.scm.entity.prod.ProductImage;
import com.mdni.scm.entity.prod.Sku;
import com.mdni.scm.entity.prod.SkuApprovalRecord;
import com.mdni.scm.service.prod.ProductService;
import com.mdni.scm.service.prod.SkuApprovalRecordService;
import com.mdni.scm.service.prod.SkuMetaService;
import com.mdni.scm.service.prod.SkuService;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <dl>
 * <dd>描述: sku RestController</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月15日 下午3:05:47</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/sku")
@SuppressWarnings("all")
public class SkuRestController extends BaseComController<SkuService, Sku> {

    private static final String PROCESS_STATUS = "processStatus";
    //作废
    public static final String SKU_VOID = "sku_void";
    //草稿
    public static final String SKU_DRAFT = "sku_draft";
    //待区域供应商审核
    public static final String SKU_SUPPLIER_AUDIT = "sku_supplier_audit";
    //待黄总审核
    public static final String SKU_YELLOW_CHECK = "sku_yellow_check";
    //待填写门店采购价 sku_store_purchase
    public static final String SKU_STORE_PURCHASE = "sku_store_purchase";
    //待审批门店采购价 sku_check_purchase
    public static final String SKU_CHECK_PURCHASE = "sku_check_purchase";
    //待填写门店销售价 sku_store_sale
    public static final String SKU_STORE_SALE = "sku_store_sale";
    //待审批门店销售价 sku_check_sale
    public static final String SKU_CHECK_SALE = "sku_check_sale";
    //未上架 sku_shelf_failure
    public static final String SKU_SHELF_FAILURE = "sku_shelf_failure";
    //上架  sku_shelf_shelves
    public static final String SKU_SHELF_SHELVES = "sku_shelf_shelves";

    @Autowired
    private SkuApprovalRecordService skuApprovalRecordService;
    @Autowired
    private SkuMetaService skuMetaService;
    @Autowired
    private ProductService productService;
    /**
     * 后台sku列表查询
     *
     * @param keyword          sku代码/sku名称
     * @param status           sku状态
     * @param catalogUrl       商品分类url
     * @param brandId          品牌Id
     *                         processStatus: '',
     * @param allSupplierId    区域供货商
     * @param allStoreCode       门店
     * @param excludeSkuIdList 需要排除的sku id列表
     * @param supplierId       供应商Id
     */
    @RequestMapping("/adminSearch")
    public Object adminSearch(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) String allSupplierId,
                              @RequestParam(required = false) Long productId,
                              @RequestParam(required = false) String allStoreCode,
                              @RequestParam(required = false) String processStatus,
                              @RequestParam(required = false) StatusEnum status, @RequestParam(required = false) String catalogUrl,
                              @RequestParam(required = false) Long supplierId, @RequestParam(required = false) Long brandId,
                              @RequestParam(required = false) List<Long> excludeSkuIdList, @RequestParam(defaultValue = "0") Integer offset,
                              @RequestParam(defaultValue = "20") Integer limit, @RequestParam(defaultValue = "id") String orderColumn,
                              @RequestParam(defaultValue = "DESC") String orderSort) {


        ShiroUser loginUser = WebUtils.getLoggedUser();
        AccoutTypeEnum acctType1 = loginUser.getAcctType();
        //空指针处理
        if(acctType1==null){
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }
        List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();

        if (!acctType1.equals(AccoutTypeEnum.MATERIAL_MANAGER)) {
            if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
                return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
            }
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));

        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "status", status);
        MapUtils.putNotNull(params, "catalogUrl", catalogUrl);
        MapUtils.putNotNull(params, "managedSupplierIdList", managedSupplierIdList);
        MapUtils.putNotNull(params, "supplierId", supplierId);
        MapUtils.putNotNull(params, "brandId", brandId);
        MapUtils.putNotNull(params, "productId", productId);
        MapUtils.putNotNull(params, "excludeSkuIdList", excludeSkuIdList);
        MapUtils.putNotNull(params, "allSupplierId", allSupplierId);
        MapUtils.putNotNull(params, "allStoreCode", allStoreCode);
        MapUtils.putNotNull(params, "processStatus", processStatus);
        AccoutTypeEnum acctType = WebUtils.getLoggedUser().getAcctType();
        BootstrapPage<Sku> productPage = service.searchScrollPage(params);
        return StatusDto.buildDataSuccessStatusDto(productPage);
    }

    @RequestMapping(value = "/modifyStock", method = RequestMethod.POST)
    public void modifyStock(@RequestParam Long skuId, @RequestParam Integer stock) {
        Sku sku = new Sku();
        sku.setId(skuId);
        sku.setStock(stock);
        this.service.update(sku);
    }

    @RequestMapping(value = "/updateSkuStatus", method = RequestMethod.POST)
    public Object updateSkuStatus(@RequestParam Long skuId, @RequestParam String processStatus) {
        Sku sku = new Sku();
        sku.setId(skuId);
        sku.setProcessStatus(processStatus);
        try {
            this.service.updateSku(sku);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }
    }

    @Override
    protected boolean isEscapeHTMLHook() {
        return false;
    }

    /**
     * @param keyword    sku代码/sku名称
     * @param status     sku状态
     * @param catalogUrl 商品分类url
     * @param brandId    品牌Id
     * @param supplierId 供应商Id
     * @author Ryze
     * @date 2017-8-15 11:54:10
     * 后台sku 审核列表查询
     */
    @RequestMapping("/approveList")
    public Object approveList(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) StatusEnum status,
                              @RequestParam(required = false) String catalogUrl,
                              @RequestParam(required = false) Long supplierId,
                              @RequestParam(required = false) Long brandId,
                              @RequestParam(required = false) String allSupplierId,
                              @RequestParam(required = false) String allStoreCode,
                              @RequestParam(required = false) String processStatus,
                              @RequestParam(defaultValue = "0") Integer offset,
                              @RequestParam(defaultValue = "20") Integer limit,
                              @RequestParam(defaultValue = "id") String orderColumn,
                              @RequestParam(defaultValue = "ASC") String orderSort) {
        ShiroUser loginUser = WebUtils.getLoggedUser();
        List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();
        if (!loginUser.getAcctType().equals(AccoutTypeEnum.CHAIRMAN_FINANCE)) {
            if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
                return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
            }
        }
        //参数组装
        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "status", status);
        MapUtils.putNotNull(params, "catalogUrl", catalogUrl);
        MapUtils.putNotNull(params, "supplierId", supplierId);
        MapUtils.putNotNull(params, "brandId", brandId);
        MapUtils.putNotNull(params, "allSupplierId", allSupplierId);
        MapUtils.putNotNull(params, "allStoreCode", allStoreCode);
        MapUtils.putNotNull(params, "processStatus", processStatus);
        MapUtils.putNotNull(params, "managedSupplierIdList", managedSupplierIdList);
        //判断登录用户类型
        AccoutTypeEnum acctType = loginUser.getAcctType();
        List<String> processStatu = new ArrayList<>();
        //区域供货商--> sku_supplier_audit 待审批
        if (AccoutTypeEnum.REGION_SUPPLIER.equals(acctType)) {
            processStatu.add(SKU_SUPPLIER_AUDIT);
        }
        //董事长财务--> sku_yellow_check 待黄总审核
        if (AccoutTypeEnum.CHAIRMAN_FINANCE.equals(acctType)) {
            processStatu.add(SKU_YELLOW_CHECK);
        }
        //门店管理员 店总-->sku_store_check 待店总审核
        if (AccoutTypeEnum.STORE.equals(acctType)) {
            processStatu.add(SKU_CHECK_PURCHASE);
            processStatu.add(SKU_CHECK_SALE);
        }
        MapUtils.putNotNull(params, PROCESS_STATUS, processStatu);
        List<Sku> pageData = Collections.emptyList();
        Long count = service.countTotal(params);
        if (count > 0) {
            pageData = service.findApproveList(params);
        }
        BootstrapPage<Sku> productPage = new BootstrapPage(count, pageData);
        return StatusDto.buildDataSuccessStatusDto(productPage);
    }

    //修改流程状态
    @RequestMapping(value = "/updateProcess")
    public Object updateProcess(@RequestParam("id") Long id,
                                @RequestParam("processStatus") String processStatus,
                                @RequestParam("remarks") String remarks,
                                @RequestParam("node") String node,
                                @RequestParam("result") String result) {
        Sku sku = new Sku();
        sku.setId(id);
        sku.setProcessStatus(processStatus);
        service.updateProcess(sku, result, remarks, WebUtils.getLoggedUser().getId() + "", node);
        return StatusDto.buildSuccessStatusDto("编辑成功！");
    }

    //获取信息sku信息
    @RequestMapping(value = "/getInfor")
    public Object getInfor(Long id) {
        Sku sku = service.getByInfoById(id);
        return StatusDto.buildDataSuccessStatusDto(sku);
    }

    /**
     * sku提交审批
     *
     * @param skuId skuId
     * @return
     */
    @RequestMapping(value = "{skuId}/submitApproval", method = RequestMethod.POST)
    public Object submitApproval(@PathVariable Long skuId) {
        Sku sku = new Sku();
        sku.setId(skuId);
        sku.setProcessStatus(SkuRestController.SKU_SUPPLIER_AUDIT);
        try {
            this.service.updateSku(sku);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }
    }

    /**
     * @param keyword    sku代码/sku名称
     * @param status     sku状态
     * @param catalogUrl 商品分类url
     * @param brandId    品牌Id
     * @param supplierId 供应商Id
     * @author Ryze
     * @date 2017-8-17 11:28:59
     * 后台sku 已审核列表查询
     */
    @RequestMapping("/checkedList")
    public Object checkedList(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) StatusEnum status,
                              @RequestParam(required = false) String catalogUrl,
                              @RequestParam(required = false) Long supplierId,
                              @RequestParam(required = false) Long brandId,
                              @RequestParam(required = false) String allSupplierId,
                              @RequestParam(required = false) String allStoreCode,
                              @RequestParam(required = false) String processStatus,
                              @RequestParam(defaultValue = "0") Integer offset,
                              @RequestParam(defaultValue = "20") Integer limit,
                              @RequestParam(defaultValue = "approval_time") String orderColumn,
                              @RequestParam(defaultValue = "DESC") String orderSort) {
        ShiroUser loginUser = WebUtils.getLoggedUser();
        List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();
        if (!loginUser.getAcctType().equals(AccoutTypeEnum.CHAIRMAN_FINANCE)) {
            if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
                return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
            }
        }
        //参数组装
        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "status", status);
        MapUtils.putNotNull(params, "catalogUrl", catalogUrl);
        MapUtils.putNotNull(params, "supplierId", supplierId);
        MapUtils.putNotNull(params, "brandId", brandId);
        MapUtils.putNotNull(params, "allSupplierId", allSupplierId);
        MapUtils.putNotNull(params, "allStoreCode", allStoreCode);
        MapUtils.putNotNull(params, "processStatus", processStatus);
        MapUtils.putNotNull(params, "userId", loginUser.getId());
        MapUtils.putNotNull(params, "managedSupplierIdList", managedSupplierIdList);
        List<Sku> pageData =null;
        Long count = service.checkTotal(params);
        if (count!=null&&count> 0) {
            pageData=service.findCheckList(params);
        }
        BootstrapPage<Sku> productPage = new BootstrapPage(count==null?0l:count, pageData);
        return StatusDto.buildDataSuccessStatusDto(productPage);
    }

    /**
     * 查看自己的审批记录列表
     *
     * @param skuId
     * @return
     */
    @RequestMapping("/recordList")
    public Object recordList(@RequestParam("skuId") Long skuId) {
        ShiroUser loginUser = WebUtils.getLoggedUser();
        List<SkuApprovalRecord> bySkuIdAndUserId = skuApprovalRecordService.findBySkuIdAndUserId(skuId, loginUser.getId() + "");
        return StatusDto.buildDataSuccessStatusDto(bySkuIdAndUserId);
    }

    /**
     * 根据状态查看审批记录列表
     *
     * @param skuId
     * @return
     */
    @RequestMapping("/recordListByStatus")
    public Object recordList(@RequestParam("skuId") Long skuId,@RequestParam(required = false) String status) {
        return StatusDto.buildDataSuccessStatusDto(skuApprovalRecordService.findResultByStatus(skuId,status));
    }

    /**
     * @param keyword    sku代码/sku名称
     * @param status     sku状态
     * @param catalogUrl 商品分类url
     * @param brandId    品牌Id
     * @param supplierId 供应商Id
     * @author Ryze
     * @date 2017-8-15 11:54:10
     * 价格审核列表查询
     */
    @RequestMapping("/priceList")
    public Object priceList(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) StatusEnum status,
                            @RequestParam(required = false) String catalogUrl,
                            @RequestParam(required = false) Long supplierId,
                            @RequestParam(required = false) Long brandId,
                            @RequestParam(required = false) String allSupplierId,
                            @RequestParam(required = false) String allStoreCode,
                            @RequestParam(required = false) String processStatus,
                            @RequestParam(defaultValue = "0") Integer offset,
                            @RequestParam(defaultValue = "20") Integer limit,
                            @RequestParam(defaultValue = "id") String orderColumn,
                            @RequestParam(defaultValue = "ASC") String orderSort) {
        ShiroUser loginUser = WebUtils.getLoggedUser();
        List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();
        if (!loginUser.getAcctType().equals(AccoutTypeEnum.CHAIRMAN_FINANCE)) {
            if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
                return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
            }
        }
        //参数组装
        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "status", status);
        MapUtils.putNotNull(params, "catalogUrl", catalogUrl);
        MapUtils.putNotNull(params, "supplierId", supplierId);
        MapUtils.putNotNull(params, "brandId", brandId);
        MapUtils.putNotNull(params, "allSupplierId", allSupplierId);
        MapUtils.putNotNull(params, "allStoreCode", allStoreCode);
        MapUtils.putNotNull(params, "processStatus", processStatus);
        MapUtils.putNotNull(params, "managedSupplierIdList", managedSupplierIdList);
        //判断登录用户类型
        AccoutTypeEnum acctType = loginUser.getAcctType();
        List<String> processStatu = new ArrayList<>();
        //区域供货商--> SKU_STORE_PURCHASE 待填写门店采购价
        if (AccoutTypeEnum.REGION_SUPPLIER.equals(acctType)) {
            processStatu.add(SKU_STORE_PURCHASE);
        }
        //材料总管--> SKU_STORE_SALE 待填写门店销售价
        if (AccoutTypeEnum.MATERIAL_MANAGER.equals(acctType)) {
            processStatu.add(SKU_STORE_SALE);
        }
        MapUtils.putNotNull(params, PROCESS_STATUS, processStatu);
        List<Sku> pageData = Collections.emptyList();
        Long count = service.countTotal(params);
        if (count > 0) {
            pageData = service.findApproveList(params);
        }
        BootstrapPage<Sku> productPage = new BootstrapPage(count, pageData);
        return StatusDto.buildDataSuccessStatusDto(productPage);
    }


    /**
     * 根据商品id 获取属性
     * @param productId 商品id
     * @author Ryze
     * @date 2017-9-18 14:54:53
     */
    @RequestMapping("/getMate/{productId}")
    public Object getMate(@PathVariable("productId") Long productId) {
        return StatusDto.buildDataSuccessStatusDto(skuMetaService.getByProductId(productId));
    }


    /**
     * 增加单个sku
     * @param
     * @author Ryze
     * @date 2017-9-18 14:54:53
     */
    @RequestMapping(value = "/saveSku")
    public Object saveSku(@RequestParam String fullPath,
                          @RequestParam String attribute1,
                          @RequestParam String attribute2,
                          @RequestParam String attribute3,
                          @RequestParam Long productId,
                          @RequestParam Integer stock) {
        Sku sku = new Sku();
        Product byId = productService.getById(productId);
        sku.setProcessStatus(SKU_DRAFT);
        sku.setProduct(byId);
        Date date = new Date();
        long time = date.getTime();
        sku.setCode("sku_"+time);
        sku.setSupplierId(byId.getSupplier().getId());
        sku.setAttribute1(attribute1);
        sku.setAttribute2(attribute2);
        sku.setAttribute3(attribute3);
        sku.setStock(stock);
        List<ProductImage> productImages=new ArrayList<ProductImage>();
        ProductImage productImage = new ProductImage();
        productImage.setImagePath(fullPath);
        productImages.add(productImage);
        sku.setProductImages(productImages);
        service.insert(sku);
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    /**
     * 作废sku
     ** @author Ryze
     * @date 2017-9-18 14:54:53
     */
    @RequestMapping(value = "/deleteSku/{id}")
    public Object deleteSku(@PathVariable("id") Long id ) {
        Sku sku = new Sku();
        sku.setProcessStatus(SKU_VOID);
        sku.setId(id);
        service.updateSku(sku);
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }


}