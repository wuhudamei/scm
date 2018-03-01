package com.damei.scm.rest.prod;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.damei.scm.common.BaseComController;
import com.damei.scm.common.Constants;
import com.damei.scm.common.dto.BootstrapPage;
import com.damei.scm.common.dto.ProductDto;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.utils.*;
import com.damei.scm.entity.account.User;
import com.damei.scm.entity.dict.DictMeasureUnit;
import com.damei.scm.entity.eum.AccoutTypeEnum;
import com.damei.scm.entity.eum.ProductStatusEnum;
import com.damei.scm.entity.eum.StatusEnum;
import com.damei.scm.entity.prod.*;
import com.damei.scm.service.dict.DictMeasureUnitService;
import com.damei.scm.service.prod.*;
import com.damei.scm.shiro.ShiroUser;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.utils.Collections3;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RestController
@RequestMapping(value = "/api/product")
public class ProductRestController extends BaseComController<ProductService, Product> {
    @Autowired
    private SkuService skuService;
    @Autowired
    private SkuMetaService skuMetaService;
    @Autowired
    private ProductImageService prodImgService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private RegionSupplierService regionService;
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private DictMeasureUnitService mergeUnitService;
    @Autowired
    private  ProductService productService;

    @RequestMapping("/adminSearch")
    public Object adminSearch(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) String allSupplierId,
                              @RequestParam(required = false) String allStoreCode,
                              @RequestParam(required = false) String catalogUrl, @RequestParam(required = false) ProductStatusEnum status,
                              @RequestParam(required = false) String supplierCodeName, @RequestParam(defaultValue = "0") Integer offset,
                              @RequestParam(defaultValue = "20") Integer limit, @RequestParam(defaultValue = "id") String orderColumn,
                              @RequestParam(defaultValue = "DESC") String orderSort) {

        ShiroUser loginUser = WebUtils.getLoggedUser();
        AccoutTypeEnum acctType1 = loginUser.getAcctType();
        if(acctType1==null){
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }

        List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();
        if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));

        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "catalogUrl", catalogUrl);
        MapUtils.putNotNull(params, "managedSupplierIdList", managedSupplierIdList);
        MapUtils.putNotNull(params, "status", status);
        MapUtils.putNotNull(params, "supplierCodeName", supplierCodeName);
        MapUtils.putNotNull(params, "allStoreCode", allStoreCode);
        MapUtils.putNotNull(params, "allSupplierId", allSupplierId);
        BootstrapPage<Product> productPage = service.adminSearch(params);

        buildMeasureUnitName(productPage.getRows());
        return StatusDto.buildDataSuccessStatusDto(productPage);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Object post(@RequestBody Product product) {
        Date date = new Date();
        long time = date.getTime();
        if (product.getHasSku() && CollectionUtils.isNotEmpty(product.getSkus())) {
            for (int idx = 0; idx < product.getSkus().size(); idx++) {
                Sku sku = product.getSkus().get(idx);
                sku.setSupplierId(product.getSupplier().getId());
                sku.setCode("sku_" + time);
                time++;
            }
        }

        Product oldProduct = this.service.getById(product.getId());

        if (!product.getHasSku()) {
            product.setSkuMeta(new SkuMeta());
            Sku sku = new Sku();
            sku.setCode("sku_" + time);
            sku.setName(product.getName());
            sku.setProcessStatus(SkuRestController.SKU_DRAFT);
            sku.setStock(0);

            if (oldProduct != null) {
                if (oldProduct.getHasSku()) {
                    skuService.deleteByProductId(oldProduct.getId());
                } else {
                    Sku oldProdSku = Collections3.getFirst(skuService.findByProductId(oldProduct.getId()));
                    if (oldProdSku != null) {
                        sku.setId(oldProdSku.getId());
                    }
                }
            }
            product.addSku(sku);
        } else {
            if (oldProduct != null && !oldProduct.getHasSku()) {
                skuService.deleteByProductId(oldProduct.getId());
            }
        }
        product.setEditor(WebUtils.getLoggedUser().valueOf());
        product.setEditTime(date);

        if (product.getId() == null) {
            if (product.getStatus() == null) {
                product.setStatus(ProductStatusEnum.LIST);
            }
            this.service.create(product);
        } else {
            this.service.update(product);
        }
        return StatusDto.buildSuccessStatusDto("商品编辑成功");
    }

    @RequestMapping(value = "/detail/{productId}", method = RequestMethod.GET)
    public Object getDetail(@PathVariable Long productId) {
        Product product = service.getProductDetailById(productId);
        if (product != null && !product.getHasSku()) {
            product.setSkus(null);
            product.setSkuMeta(null);
        }

        return StatusDto.buildDataSuccessStatusDto(product);
    }

    @RequestMapping(value = "/{productId}/submitToAudit", method = RequestMethod.GET)
    public Object submitToAudit(@PathVariable Long productId) {
        Product product = service.getById(productId);
        if (product == null) {
            return StatusDto.buildFailureStatusDto("商品不存在,productId " + productId);
        }

        Product updateStatus = new Product();
        updateStatus.setId(productId);
        service.update(updateStatus);
        return StatusDto.buildSuccessStatusDto("商品提交审核成功");
    }

    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public Object approveProduct(@RequestParam Long productId, @RequestParam Boolean passResult) {
        Product product = service.getById(productId);
        if (product == null) {
            return StatusDto.buildFailureStatusDto("商品不存在,productId " + productId);
        }


        Product updateStatus = new Product();
        updateStatus.setStatus(passResult ? ProductStatusEnum.LIST : ProductStatusEnum.DELIST);
        updateStatus.setId(productId);
        service.update(updateStatus);
        return StatusDto.buildSuccessStatusDto("审批操作成功");
    }

    @RequestMapping(value = "{productId}/soldout", method = RequestMethod.GET)
    public Object soldout(@PathVariable Long productId){
        Product product = service.getById(productId);
        if (product == null) {
            return StatusDto.buildFailureStatusDto("商品不存在,productId " + productId);
        }

        if (!ProductStatusEnum.DELIST.equals(product.getStatus())) {
            return StatusDto.buildFailureStatusDto("只有下架商品才能上架");
        }
        Product updateStatus = new Product();
        updateStatus.setStatus(ProductStatusEnum.LIST);
        updateStatus.setId(productId);
        service.update(updateStatus);
        return StatusDto.buildSuccessStatusDto("商品上架成功");
    }


    @RequestMapping(value = "{productId}/delist", method = RequestMethod.GET)
    public Object deList(@PathVariable Long productId) {
        Product product = service.getById(productId);
        if (product == null) {
            return StatusDto.buildFailureStatusDto("商品不存在,productId " + productId);
        }

        if (!ProductStatusEnum.LIST.equals(product.getStatus())) {
            return StatusDto.buildFailureStatusDto("只有上架商品才能下架");
        }

        Product updateStatus = new Product();
        updateStatus.setStatus(ProductStatusEnum.DELIST);
        updateStatus.setId(productId);
        service.update(updateStatus);
        List<Sku> skus = this.skuService.findByProductId(productId);
        for(Sku sku : skus){
            if("sku_shelf_shelves".equals(sku.getProcessStatus())){
                sku.setProcessStatus("sku_shelf_failure");
            }
            this.skuService.updateSku(sku);
        }
        return StatusDto.buildSuccessStatusDto("下架操作成功");
    }

    @RequestMapping(value = "{productId}/reject", method = RequestMethod.POST)
    public Object reject(@PathVariable Long productId) {
        Product product = service.getById(productId);
        if (product == null) {
            return StatusDto.buildFailureStatusDto("商品不存在,productId " + productId);
        }

        if (!ProductStatusEnum.DELIST.equals(product.getStatus())) {
            return StatusDto.buildFailureStatusDto("只有下架后 才能打回到商品供货商");
        }

        Product updateStatus = new Product();
        updateStatus.setStatus(ProductStatusEnum.LIST);
        updateStatus.setId(productId);
        service.update(updateStatus);
        return StatusDto.buildSuccessStatusDto("打回操作成功");
    }

    @RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
    public void downloadProductTemplate(HttpServletResponse resp) {
        final String tempFileName = "product_template.xlsx";
        ServletOutputStream out = null;
        resp.setContentType("application/x-msdownload");
        resp.addHeader("Content-Disposition", "attachment; filename='" + tempFileName + "'");
        Resource resource = new DefaultResourceLoader().getResource(tempFileName);
        File randomCopyFile = null;
        Workbook wb = null;
        try {
            out = resp.getOutputStream();
            File file = resource.getFile();
            String randomCopyFileName = UUID.randomUUID().toString() + ".xlsx";
            randomCopyFile = new File(file.getParent(), randomCopyFileName);
            FileUtils.copyFile(file, randomCopyFile);
            wb = WorkbookFactory.create(randomCopyFile);

            writeProductDictDataToSheet(wb.getSheetAt(1), catalogService.findLeafCatalogList(StatusEnum.OPEN), "name");

            writeRegionAndSupplierToSheet(wb.getSheetAt(2));

            final String name = "brandName";
            writeProductDictDataToSheet(wb.getSheetAt(3), brandService.findAll(), name);

            final String name1 = "name";
            writeProductDictDataToSheet(wb.getSheetAt(4), mergeUnitService.findAll(), name1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                wb.write(out);
                wb.close();
            } catch (IOException e) {
            }
            if (randomCopyFile != null) {
                FileUtils.deleteFile(randomCopyFile);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Object importProduct(@RequestParam("file") MultipartFile file) {
        List<ProductDto> productDtoList = null;
        try {
            productDtoList = ExcelUtil.getInstance().readExcel2ObjsByStream(file.getInputStream(), ProductDto.class);
        } catch (IOException e) {
        }
        if (CollectionUtils.isEmpty(productDtoList)) {
            return StatusDto.buildFailureStatusDto("Excel文件中没有任何商品数据");
        }

        Map<String, Map<String, Supplier>> regionMap = convertSupplierToNameMap(this.regionService
                .findRegionWithSuppliers(null));
        final String name = "name";
        Map<String, Catalog> catalogNameMap = convertListToNameMap(this.catalogService.findLeafCatalogList(null), name);
        final String name1 = "brandName";
        Map<String, Brand> brandNameMap = convertListToNameMap(this.brandService.findAll(), name1);
        Map<String, DictMeasureUnit> measureUnitMap = convertListToNameMap(this.mergeUnitService.findAll(), name);
        Map<String, Sku> skuCodeMap = convertSkuListToCodeMap(this.skuService.findAll());

        String errorMsg = validateImportExcelProductData(productDtoList, regionMap, catalogNameMap, brandNameMap,
                measureUnitMap, skuCodeMap);
        if (StringUtils.isNotEmpty(errorMsg)) {
            return StatusDto.buildFailureStatusDto(errorMsg);
        }

        executeEncapsulateProduct(productDtoList, regionMap, catalogNameMap, brandNameMap, measureUnitMap, skuCodeMap);
        return StatusDto.buildSuccessStatusDto("商品批量导入成功");
    }

    private void executeEncapsulateProduct(List<ProductDto> productDtoList,
                                           Map<String, Map<String, Supplier>> regionMap, Map<String, Catalog> catalogNameMap,
                                           Map<String, Brand> brandNameMap, Map<String, DictMeasureUnit> measureUnitMap, Map<String, Sku> skuCodeMap) {

        User editor = WebUtils.getLoggedUser().valueOf();
        Date editTime = new Date();
        long time = editTime.getTime();
        List<Product> list = new ArrayList<Product>();
        for (ProductDto productDto : productDtoList) {
            Product product = new Product();
            product.setEditor(editor);
            product.setEditTime(editTime);
            product.setBrand(brandNameMap.get(productDto.getBrandName()));
            product.setCatalog(catalogNameMap.get(productDto.getCatalogName()));
            product.setMeasureUnit(measureUnitMap.get(productDto.getMeasureUnitName()));

            product.setSupplier(regionMap.get(productDto.getRegionName()).get(productDto.getSupplierName()));
            product.setStatus(ProductStatusEnum.LIST);
            SkuMeta skuMeta = new SkuMeta();
            try {
                BeanUtils.copyProperties(product, productDto);
                BeanUtils.copyProperties(skuMeta, productDto);
            } catch (IllegalAccessException | InvocationTargetException e) {
            }
            product.setSkuMeta(skuMeta);
            if (!StringUtils.isAnyEmpty(skuMeta.getAttribute1Name(), skuMeta.getAttribute2Name(),
                    skuMeta.getAttribute3Name())) {
                product.setHasSku(Boolean.FALSE);
            } else {
                product.setHasSku(Boolean.TRUE);
            }
            String attribute1 = productDto.getAttribute1();
            String[] split1 = null;
            String attribute2 = productDto.getAttribute2();
            String[] split2 = null;
            String attribute3 = productDto.getAttribute3();
            String[] split3 = null;
            if (!StringUtils.isEmpty(attribute1)) {
                String replace = attribute1.replace(" ", "");
                String s = replace.replaceAll("，", ",");
                split1 = s.split(",");
            }
            if (!StringUtils.isEmpty(attribute2)) {
                String replace = attribute2.replace(" ", "");
                String s = replace.replaceAll("，", ",");
                split2 = s.split(",");
            }
            if (!StringUtils.isEmpty(attribute3)) {
                String replace = attribute3.replace(" ", "");
                String s = replace.replaceAll("，", ",");
                split3 = s.split(",");
            }
            String[] add = null;
            if (split1 != null) {
                if (split2 != null) {
                    if (split3 != null) {
                        add = turns(split1, split2, split3);
                    } else {
                        add = turns(split1, split2);
                    }
                } else {
                    add = turns(split1);
                }
            } else {
                if (split2 != null) {
                    if (split3 != null) {
                        add = turns(split2, split3);
                    } else {
                        add = turns(split2);
                    }
                } else {
                    if (split3 != null) {
                        add = turns(split3);
                    }
                }

            }
            ArrayList<Sku> skus = new ArrayList<>();
            for (String str : add) {
                if (!StringUtils.isEmpty(str)) {
                    Sku sku = new Sku();
                    sku.setProcessStatus(SkuRestController.SKU_DRAFT);
                    String[] split = str.split(",");
                    int length = split.length;
                    switch (length) {
                        case 1: {
                            sku.setAttribute1(split[0]);
                            break;
                        }
                        case 2: {
                            sku.setAttribute1(split[0]);
                            sku.setAttribute2(split[1]);
                            break;
                        }
                        case 3: {
                            sku.setAttribute1(split[0]);
                            sku.setAttribute2(split[1]);
                            sku.setAttribute3(split[2]);
                            break;
                        }

                    }
                    String code = "sku_" + time;
                    sku.setCode(code);
                    skus.add(sku);
                    time+=1l;
                }
            }

            product.setSkus(skus);
            list.add(product);
        }

        for (Product product : list) {
            this.service.create(product);
        }

    }


    private String[] doubleTurns(String[] array1, String[] array2) {
        String[] target = new String[array1.length * array2.length];
        for (int i = 0, a1 = 0, a2 = 0; i < array1.length * array2.length; i++) {
            target[i] = array1[a1] + "," + array2[a2];
            a2++;
            if (a2 == array2.length) {
                a2 = 0;
                a1++;
            }
        }
        return target;
    }
    private String[] turns(String[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }
        if (arrays.length == 0) {
            return null;
        }
        int count = 0;
        for (int i = 0; i < arrays.length; i++) {
            count *= arrays[i].length;
        }
        String target[] = new String[count];
        for (int i = 0; i < arrays.length; i++) {
            if (i == 0) {
                target = doubleTurns(arrays[0], arrays[1]);
                i++;
            } else {
                target = doubleTurns(target, arrays[i]);
            }
        }
        return target;
    }


    private String validateImportExcelProductData(List<ProductDto> productDtoList,
                                                  Map<String, Map<String, Supplier>> regionMap, Map<String, Catalog> catalogNameMap,
                                                  Map<String, Brand> brandNameMap, Map<String, DictMeasureUnit> measureUnitMap, Map<String, Sku> skuCodeMap) {

        int rowIndex = 1;
        String errorMsg = null;

        final String rowNumKey = "rowNum";
        final String errorMsgKey = "errorMsg";
        //第rowNum行{errorMsg}
        String errorMsgTmp = "第" + rowNumKey + "行{" + errorMsgKey + "}";


        StringBuilder errorBuf = new StringBuilder();
        for (ProductDto productDto : productDtoList) {


            Long supplierId = null;


            if (StringUtils.isBlank(productDto.getName())) {
                errorBuf.append("商品名称不能为空;");
            }


            if (StringUtils.isBlank(productDto.getRegionName())) {
                errorBuf.append("区域供应商不能为空;");
            } else if (!regionMap.containsKey(productDto.getRegionName())) {
                errorBuf.append("区域供应商不存在;");
            }

            if (StringUtils.isBlank(productDto.getSupplierName())) {
                errorBuf.append("商品供货商不能为空;");
            } else {
                Map<String, Supplier> supplierMap = regionMap.get(productDto.getRegionName());
                if (supplierMap != null && !supplierMap.containsKey(productDto.getSupplierName())) {
                    errorBuf.append("商品供货商不存在;");
                }

                if (supplierMap != null && supplierMap.containsKey(productDto.getSupplierName())) {
                    supplierId = supplierMap.get(productDto.getSupplierName()).getId();
                }
            }


            if (StringUtils.isBlank(productDto.getCatalogName())) {
                errorBuf.append("商品分类不能为空;");
            } else if (!catalogNameMap.containsKey(productDto.getCatalogName())) {
                errorBuf.append("商品分类不存在;");
            }


            if (StringUtils.isBlank(productDto.getBrandName())) {
                errorBuf.append("品牌名称不能为空;");
            } else if (!brandNameMap.containsKey(productDto.getBrandName())) {
                errorBuf.append("品牌名称不存在;");
            }


            if (StringUtils.isBlank(productDto.getMeasureUnitName())) {
                errorBuf.append("计量单位不能为空;");
            } else if (!measureUnitMap.containsKey(productDto.getMeasureUnitName())) {
                errorBuf.append("计量单位不存在;");
            }

            rowIndex++;
            if (errorBuf.length() > 0) {
                break;
            }
        }
        if (errorBuf.length() > 0) {
            errorMsg = errorMsgTmp.replace(rowNumKey, String.valueOf(rowIndex)).replace(errorMsgKey,
                    errorBuf.toString());
        }

        return errorMsg;
    }

    private <E> Map<String, E> convertListToNameMap(List<E> entityList, final String name) {
        return Maps.uniqueIndex(entityList, new Function<E, String>() {
            @Override
            public String apply(E input) {
                return ReflectionUtils.getFieldValue(input, name).toString();
            }
        });
    }

    private <E> Map<String, Sku> convertSkuListToCodeMap(List<Sku> skuList) {
        return Maps.uniqueIndex(skuList, new Function<Sku, String>() {
            @Override
            public String apply(Sku input) {
                return input.getSupplierId() + "_" + input.getCode();
            }
        });
    }

    private Map<String, Map<String, Supplier>> convertSupplierToNameMap(List<RegionSupplier> regionSupplierList) {

        Map<String, Map<String, Supplier>> regionMap = Maps.newHashMap();
        for (RegionSupplier regionSupplier : regionSupplierList) {
            Map<String, Supplier> supplierMap = regionMap.get(regionSupplier.getName());
            if (supplierMap == null) {
                supplierMap = Maps.newHashMap();
                regionMap.put(regionSupplier.getName(), supplierMap);
            }

            if (CollectionUtils.isNotEmpty(regionSupplier.getSupplierList())) {
                for (Supplier supplier : regionSupplier.getSupplierList()) {
                    supplierMap.put(supplier.getName(), supplier);
                }
            }
        }
        return regionMap;
    }

    private void writeProductDictDataToSheet(Sheet sheet, List entityList, final String name) {
        if (CollectionUtils.isNotEmpty(entityList)) {
            for (int i = 0; i < entityList.size(); i++) {
                Row curRow = sheet.createRow(i + 1);
                Cell cell = curRow.createCell(0);
                cell.setCellValue(ReflectionUtils.getFieldValue(entityList.get(i), name).toString());
            }
        }
    }

    private void writeRegionAndSupplierToSheet(Sheet sheet) {
        List<RegionSupplier> regionList = regionService.findRegionWithSuppliers(StatusEnum.OPEN);
        if (CollectionUtils.isNotEmpty(regionList)) {
            int rowIndex = 1;
            for (RegionSupplier region : regionList) {
                int mergeStartRowIndex = rowIndex;
                for (Supplier supplier : region.getSupplierList()) {
                    Row curRow = sheet.createRow(rowIndex);
                    Cell regionNameCell = curRow.createCell(0);
                    regionNameCell.setCellValue(region.getName());
                    Cell supplierNameCell = curRow.createCell(1);
                    supplierNameCell.setCellValue(supplier.getName());
                    rowIndex++;
                }
                int mergeEndRowIndex = rowIndex - 1;
                CellRangeAddress mergeRange = new CellRangeAddress(mergeStartRowIndex, mergeEndRowIndex, 0, 0);
                sheet.addMergedRegion(mergeRange);
            }
        }
    }

    private void buildMeasureUnitName(List<Product> productList) {
        if (CollectionUtils.isNotEmpty(productList)) {

            Map<Long, DictMeasureUnit> measureUnitMap = Maps.uniqueIndex(this.mergeUnitService.findAll(),
                    new Function<DictMeasureUnit, Long>() {
                        @Override
                        public Long apply(DictMeasureUnit input) {
                            return input.getId();
                        }
                    });

            for (Product product : productList) {
                product.setMeasureUnit(measureUnitMap.get(product.getId()));
            }
        }
    }

    @Override
    protected boolean isEscapeHTMLHook() {
        return false;
    }


    @RequestMapping(value = "{productId}/batchSku/{flag}", method = RequestMethod.GET)
    public Object batchSku(@PathVariable Long productId,@PathVariable String flag) {
        this.service.batchSku(productId,flag);
        return StatusDto.buildSuccessStatusDto("操作成功");

    }

}