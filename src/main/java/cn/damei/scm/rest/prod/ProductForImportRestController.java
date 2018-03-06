package cn.damei.scm.rest.prod;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.dto.ProductForExcuteDto;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.ExcelUtil;
import cn.damei.scm.common.utils.FileUtils;
import cn.damei.scm.common.utils.ReflectionUtils;
import cn.damei.scm.entity.account.User;
import cn.damei.scm.entity.dict.DictMeasureUnit;
import cn.damei.scm.entity.eum.PriceTypeEnum;
import cn.damei.scm.entity.eum.ProductStatusEnum;
import cn.damei.scm.entity.eum.StatusEnum;
import cn.damei.scm.entity.prod.*;
import cn.damei.scm.service.dict.DictMeasureUnitService;
import cn.damei.scm.service.prod.*;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import cn.damei.scm.common.utils.WebUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("all")
@RestController
@RequestMapping(value = "/api/productimport")
public class ProductForImportRestController extends BaseComController<ProductService, Product> {
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

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Object importProduct(@RequestParam("file") MultipartFile file) {
        List<ProductForExcuteDto> productDtoList = null;
        try {
            productDtoList = ExcelUtil.getInstance().readExcel2ObjsByStream(file.getInputStream(), ProductForExcuteDto.class);
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
        List<String> strings = validateImportExcelProductData(productDtoList, regionMap, catalogNameMap, brandNameMap,
                measureUnitMap, skuCodeMap);
        if (strings!=null && strings.size()>0) {
            return StatusDto.buildDataSuccessStatusDto("校验未通过",strings);
        }else {
            executeEncapsulateProduct(productDtoList, regionMap, catalogNameMap, brandNameMap, measureUnitMap, skuCodeMap);
        }
        List<String> strings1 = new ArrayList<>();
        strings1.add("商品批量导入成功");
        return StatusDto.buildDataSuccessStatusDto("商品批量导入成功",strings1);
    }
    @RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
    public void downloadProductTemplate(HttpServletResponse resp) {
        final String tempFileName = "product_for_excute.xlsx";
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
    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public Object executeProduct(@RequestParam("file") MultipartFile file) {
        List<ProductForExcuteDto> productDtoList = null;
        try {
            productDtoList = ExcelUtil.getInstance().readExcel2ObjsByStream(file.getInputStream(), ProductForExcuteDto.class);
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

        List<String> errorMsg = validateImportExcelProductData(productDtoList, regionMap, catalogNameMap, brandNameMap,
                measureUnitMap, skuCodeMap);
        if (errorMsg!=null && errorMsg.size()>0) {
            return StatusDto.buildDataSuccessStatusDto(errorMsg);
        }else {
            List<String> strings1 = new ArrayList<>();
            strings1.add("商品校验通过");
            return StatusDto.buildDataSuccessStatusDto(strings1);
        }
    }

    private void executeEncapsulateProduct(List<ProductForExcuteDto> productDtoList,
                                           Map<String, Map<String, Supplier>> regionMap, Map<String, Catalog> catalogNameMap,
                                           Map<String, Brand> brandNameMap, Map<String, DictMeasureUnit> measureUnitMap, Map<String, Sku> skuCodeMap) {
        Long id = WebUtils.getLoggedUser().getId();
        User user = new User();
        user.setId(id);
        User editor = WebUtils.getLoggedUser().valueOf();
        Date editTime = new Date();
        long time = editTime.getTime();
        List<Product> list = new ArrayList<Product>();
        for (ProductForExcuteDto productDto : productDtoList) {
            Double supplyPrice = productDto.getSupplyPrice();
            Double storePrice = productDto.getStorePrice();
            Double salePrice = productDto.getSalePrice();
            Double upgradePrice = productDto.getUpgradePrice();
            Double addPrice = productDto.getAddPrice();
            Double reducePrice = productDto.getReducePrice();
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
                Date date = new Date();
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
                    List<SkuPrice> skuPrices = new ArrayList<>();
                    if(supplyPrice!=null){
                        SkuPrice skuPrice = new SkuPrice();
                        skuPrice.setPriceType(PriceTypeEnum.SUPPLY);
                        skuPrice.setPrice(BigDecimal.valueOf(supplyPrice));
                        skuPrice.setPriceStartDate(date);
                        skuPrice.setEditTime(date);
                        skuPrice.setEditor(user);
                        skuPrices.add(skuPrice);
                    }
                    if(storePrice!=null){
                        SkuPrice skuPrice = new SkuPrice();
                        skuPrice.setPriceType(PriceTypeEnum.STORE);
                        skuPrice.setPrice(BigDecimal.valueOf(storePrice));
                        skuPrice.setPriceStartDate(date);
                        skuPrice.setEditTime(date);
                        skuPrice.setEditor(user);
                        skuPrices.add(skuPrice);
                    }
                    if(salePrice!=null){
                        SkuPrice skuPrice = new SkuPrice();
                        skuPrice.setPriceType(PriceTypeEnum.SALE);
                        skuPrice.setPrice(BigDecimal.valueOf(salePrice));
                        skuPrice.setPriceStartDate(date);
                        skuPrice.setEditTime(date);
                        skuPrice.setEditor(user);
                        skuPrices.add(skuPrice);
                    }
                    if(upgradePrice!=null){
                        SkuPrice skuPrice = new SkuPrice();
                        skuPrice.setPriceType(PriceTypeEnum.UPGRADE);
                        skuPrice.setPrice(BigDecimal.valueOf(upgradePrice));
                        skuPrice.setPriceStartDate(date);
                        skuPrice.setEditTime(date);
                        skuPrice.setEditor(user);
                        skuPrices.add(skuPrice);
                    }
                    if(addPrice!=null){
                        SkuPrice skuPrice = new SkuPrice();
                        skuPrice.setPriceType(PriceTypeEnum.INCREASED);
                        skuPrice.setPrice(BigDecimal.valueOf(addPrice));
                        skuPrice.setPriceStartDate(date);
                        skuPrice.setEditTime(date);
                        skuPrice.setEditor(user);
                        skuPrices.add(skuPrice);
                    }
                    if(reducePrice!=null){
                        SkuPrice skuPrice = new SkuPrice();
                        skuPrice.setPriceType(PriceTypeEnum.MINUS);
                        skuPrice.setPrice(BigDecimal.valueOf(reducePrice));
                        skuPrice.setPriceStartDate(date);
                        skuPrice.setEditTime(date);
                        skuPrice.setEditor(user);
                        skuPrices.add(skuPrice);
                    }
                    sku.setPriceList(skuPrices);
                    String code = "sku_" + time;
                    sku.setCode(code);
                    skus.add(sku);
                    time+=1L;
                }
            }
            product.setSkus(skus);
            list.add(product);
        }
        //保存商品
        for (Product product : list) {
            this.service.createSkuAndPrice(product);
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

    private List<String> validateImportExcelProductData(List<ProductForExcuteDto> productDtoList,
                                                  Map<String, Map<String, Supplier>> regionMap, Map<String, Catalog> catalogNameMap,
                                                  Map<String, Brand> brandNameMap, Map<String, DictMeasureUnit> measureUnitMap, Map<String, Sku> skuCodeMap) {
        int rowIndex = 1;
        final String rowNumKey = "rowNum";
        final String errorMsgKey = "errorMsg";
        List<String> returnList = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (ProductForExcuteDto productDto : productDtoList) {
            StringBuilder errorBuf = new StringBuilder();
            String errorMsg = null;
            String errorMsgTmp = "第" + rowNumKey + "行{" + errorMsgKey + "}";
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
            String strSupplyPrice = productDto.getStrSupplyPrice();
            String strStorePrice = productDto.getStrStorePrice();
            String strSalePrice = productDto.getStrSalePrice();
            String strUpgradePrice = productDto.getStrUpgradePrice();
            String strAddPrice = productDto.getStrAddPrice();
            String strReducePrice = productDto.getStrReducePrice();
            if(strSupplyPrice ==null){
                errorBuf.append("网真采购价没填写;");
            }else {
                try {
                    productDto.setSupplyPrice(Double.valueOf(strSupplyPrice));
                }catch (Exception e){
                    errorBuf.append("网真采购价格式不正确;");
                }
            }
            if(strStorePrice ==null){
                errorBuf.append("门店采购价没填写;");
            }else{
                try {
                    productDto.setStorePrice(Double.valueOf(strStorePrice));
                }catch (Exception e){
                    errorBuf.append("门店采购价式不正确;");
                }
            }
            if(strSalePrice ==null){
                errorBuf.append("门店销售价没填写;");
            }else{
                try {
                    productDto.setSalePrice(Double.valueOf(strSalePrice));
                }catch (Exception e){
                    errorBuf.append("门店销售价格式不正确;");
                }
            }
            if( StringUtils.isNotBlank( strUpgradePrice ) ){
                try {
                    productDto.setUpgradePrice(Double.valueOf(strUpgradePrice));
                }catch (Exception e){
                    errorBuf.append("升级价格式不正确;");
                }
            }
            if( StringUtils.isNotBlank( strAddPrice ) ){
                try {
                    productDto.setAddPrice(Double.valueOf(strAddPrice));
                }catch (Exception e){
                    errorBuf.append("增项价格式不正确;");
                }
            }
            if( StringUtils.isNotBlank( strReducePrice ) ){
                try {
                    productDto.setReducePrice(Double.valueOf(strReducePrice));
                }catch (Exception e){
                    errorBuf.append("减项价格式不正确;");
                }
            }
            Double supplyPrice = productDto.getSupplyPrice();
            Double storePrice = productDto.getStorePrice();
            Double salePrice = productDto.getSalePrice();
            Double upgradePrice = productDto.getUpgradePrice();
            Double addPrice = productDto.getAddPrice();
            Double reducePrice = productDto.getReducePrice();
            if(upgradePrice!=null && upgradePrice<0){
                errorBuf.append("升级价不可以小于0;");
            }
            if(addPrice!=null && addPrice<0){
                errorBuf.append("增项价不可以小于0;");
            }
            if(reducePrice!=null && reducePrice<0){
                errorBuf.append("减项价不可以小于0;");
            }
            if(supplyPrice!=null && storePrice !=null && salePrice!=null){
                if(supplyPrice<0){
                    errorBuf.append("网真采购价不可以小于0;");
                }
                if(storePrice<0){
                    errorBuf.append("门店采购价不可以小于0;");
                }
                if(salePrice<0){
                    errorBuf.append("门店销售价不可以小于0;");
                }
                if(storePrice<supplyPrice){
                    errorBuf.append("门店采购价不能小于网真采购价;");
                }
                if(salePrice<storePrice){
                    errorBuf.append("门店销售价不能小于门店采购价;");
                }
            }
            if (errorBuf.length() > 0) {
                errorMsg = errorMsgTmp.replace(rowNumKey, String.valueOf(rowIndex+1)).replace(errorMsgKey,
                        errorBuf.toString());
                returnList.add(errorMsg);
            }
            rowIndex++;
        }
        return returnList;
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

}