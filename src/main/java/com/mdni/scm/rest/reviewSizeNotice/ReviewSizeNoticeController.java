package com.mdni.scm.rest.reviewSizeNotice;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.PropertyHolder;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.MutipleDataStatusDto;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.FileUtils;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.eum.ReviewSizeNoticeEnum;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.operateLog.OperateLog;
import com.mdni.scm.entity.prod.Brand;
import com.mdni.scm.entity.prod.Supplier;
import com.mdni.scm.entity.reviewSizeNotice.ReviewSizeNotice;
import com.mdni.scm.service.operatorLog.OperateLogService;
import com.mdni.scm.service.prod.BrandService;
import com.mdni.scm.service.prod.SupplierService;
import com.mdni.scm.service.reviewSizeNotice.ReviewSizeNoticeService;
import com.mdni.scm.service.upload.UploadService;
import com.mdni.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * <dl>
 * <dd>描述: 复尺申请controller</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年7月28日 下午1:13:51</dd>
 * <dd>创建人： Mark</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/reviewSize")
public class ReviewSizeNoticeController extends BaseComController<ReviewSizeNoticeService, ReviewSizeNotice> {

    @Autowired
    private UploadService uploadService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OperateLogService operateLogService;


    @RequestMapping("/list/{contractCode}")
    public Object list(@PathVariable String contractCode,
                       @RequestParam(defaultValue = "0") int offset,
                       @RequestParam(defaultValue = "id") String orderColumn,
                       @RequestParam(defaultValue = "DESC") String orderSort,
                       @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
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
     * @param path 图片路径
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Object delete(@RequestParam String path) {
        uploadService.delete(path);
        return StatusDto.buildSuccessStatusDto();
    }


    /**
     * 查询供应商
     */
    @RequestMapping("/supplierList")
    public Object supplierList(@RequestParam(required = false) String name,
                               @RequestParam(defaultValue = "0") int offset,
                               @RequestParam(defaultValue = "id") String orderColumn,
                               @RequestParam(defaultValue = "DESC") String orderSort,
                               @RequestParam(defaultValue = "20") int limit) {


        Map<String, Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params,"storeCode",WebUtils.getLoggedUser().getStoreCode());
        MapUtils.putNotNull(params, "keyword", name);
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(supplierService.getByStoreCode(params));
    }

    /**
     * 新增复尺申请
     * @param list 复尺申请信息
     * @return
     */
    @RequestMapping("/insert")
    public Object save(@RequestBody List<ReviewSizeNotice> list) {
        String name = WebUtils.getLoggedUser().getName();
        Date date = new Date();
        for (ReviewSizeNotice reviewSizeNotice : list) {
            reviewSizeNotice.setCreateName(name);
            reviewSizeNotice.setCreateTime(date);
            reviewSizeNotice.setNoticeTime(date);
            reviewSizeNotice.setReviewStatus(ReviewSizeNoticeEnum.NORIVEEWSIZE);
            this.service.insert(reviewSizeNotice);
            //增加复尺通知单日志
            OperateLog operateLog = new OperateLog();
            operateLog.setOperator(WebUtils.getLoggedUser().getName());
            operateLog.setOperatorTime(new Date());
            operateLog.setOperatorExplain("创建了复尺通知单");
            operateLog.setContractCode(reviewSizeNotice.getContractCode());
            operateLogService.insert(operateLog);
        }
        return StatusDto.buildDataSuccessStatusDto("复尺申请成功!");
    }

    /**
     * 查询所有复尺申请
     * @return
     */
    @RequestMapping("/all")
    public Object findAllReviewSize(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Date endDate,
                                    @RequestParam(required = false) Long brandId,
                                    @RequestParam(required = false) Date startDate,
                                    @RequestParam(required = false) String status,
                                    @RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "id") String orderColumn,
                                    @RequestParam(defaultValue = "DESC") String orderSort,
                                    @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "endDate", endDate);
        MapUtils.putNotNull(paramMap, "startDate", startDate);
        MapUtils.putNotNull(paramMap, "brandId", brandId);
        if(!"".equals(status))
            MapUtils.putNotNull(paramMap, "status", ReviewSizeNoticeEnum.values()[Integer.parseInt(status)].name());
        ShiroUser shiroUser = WebUtils.getLoggedUser();
        if(AccoutTypeEnum.PROD_SUPPLIER.equals(shiroUser.getAcctType())){
            paramMap.put("supplierId",shiroUser.getSupplierId());
        }else if(AccoutTypeEnum.MATERIAL_CLERK.equals(shiroUser.getAcctType())){
            paramMap.put("name",shiroUser.getName());
        }
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
    }

    /**
     * 根据项目号查询复尺申请
     * @param contractId
     * @return
     */
    @RequestMapping("/getByContractId")
    public Object getByContractId(@RequestParam(required = false) String contractId){
        List<ReviewSizeNotice> reviewSizeNoticeList = this.service.getByContractId(contractId);
        for(ReviewSizeNotice reviewSizeNotice : reviewSizeNoticeList){
            Brand brand = brandService.getById(Long.parseLong(reviewSizeNotice.getBrandId()));//根据品牌id获得品牌信息
            reviewSizeNotice.setBrandId(brand.getBrandName());
            reviewSizeNotice.setBrand(brand);
            Supplier supplier = supplierService.getById(Long.parseLong(reviewSizeNotice.getSupplierId()));
            reviewSizeNotice.setSupplierId(supplier.getName());
            reviewSizeNotice.setSupplier(supplier);
        }
        return StatusDto.buildDataSuccessStatusDto(reviewSizeNoticeList);
    }

    /**
     * 更新复尺申请
     * @param list 复尺申请信息
     * @return
     */
    @RequestMapping("/edit")
    public Object edit(@RequestBody  List<ReviewSizeNotice> list){
        for(ReviewSizeNotice reviewSizeNotices : list){
            this.service.update(reviewSizeNotices);
        }
        return StatusDto.buildDataSuccessStatusDto("复尺申请成功!");

    }

    @RequestMapping("/getReviewSizeNorice")
    public  Object getReviewSizeNorice(@RequestParam(required = true) Long contractId,
                                        @RequestParam(defaultValue = "0") int offset,
                                       @RequestParam(defaultValue = "id") String orderColumn,
                                       @RequestParam(defaultValue = "DESC") String orderSort,
                                       @RequestParam(defaultValue = "20") int limit){
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap,"contractId",contractId);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        BootstrapPage<ReviewSizeNotice> reviewSizeNoticeBootstrapPage = service.searchScrollPage(paramMap);
        return StatusDto.buildDataSuccessStatusDto(reviewSizeNoticeBootstrapPage);
    }

    /**
     * 根据id更新复尺通知单状态
     * @param noticeId
     * @param reviewStatus
     * @return
     */
    @RequestMapping(value = "updateNoriceStatus",method = RequestMethod.POST)
    public Object updateStatus(@RequestParam(required = true)  Long noticeId,
                               @RequestParam(required = true)  String contractCode,
                               @RequestParam(required = true)  ReviewSizeNoticeEnum reviewStatus){
        this.service.updateReviewStatus(noticeId,reviewStatus);
        return StatusDto.buildSuccessStatusDto("操作成功！");
    }

    /**
     * 上传复尺记录
     * @author Ryze
     */
    @Transactional
    @RequestMapping(value = "/upload/record/{id}",method = RequestMethod.POST)
    public Object uploadRecording(@PathVariable("id") Long id,MultipartFile file,
                         @RequestParam UploadService.UploadCategory type) {
        String saveTmpPath = StringUtils.EMPTY;
        try {
            String path = saveTmpPath = uploadService.upload(file, type);
            ReviewSizeNotice reviewSizeNotice = new ReviewSizeNotice();
            reviewSizeNotice.setId(id);
            reviewSizeNotice.setUploadUrl(path);
            reviewSizeNotice.setReviewStatus(ReviewSizeNoticeEnum.YESRIVEEWSIZE);
            this.service.update(reviewSizeNotice);
        } catch (Exception e) {
            return StatusDto.buildFailureStatusDto(e.getMessage());
        }
        return StatusDto.buildDataSuccessStatusDto("成功");

    }
    /**
     * * @author Ryze
     * 下载复尺记录
     */
    @RequestMapping(value = "/download/record/{id}",method = RequestMethod.GET)
    public void uploadRecording(@PathVariable("id") Long id,HttpServletResponse resp) {
        ReviewSizeNotice byId = this.service.getById(id);
        //判断文件是否存在
        String uploadUrl = byId.getUploadUrl();
        String path = PropertyHolder.getUploadDir() + "/" + uploadUrl;
        File randomCopyFile = null;
        Workbook wb = null;
        if(FileUtils.isFileExists(path)){
            File file = FileUtils.getFileByPath(path);
            String name = file.getName();
            String prefix=name.substring(name.lastIndexOf(".")+1);
            ServletOutputStream out = null;
            try {
                resp.setContentType("application/x-msdownload;charset=UTF-8");
                resp.addHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode("复尺详情."+prefix, "UTF-8")+ "\"");
                out = resp.getOutputStream();
                wb = WorkbookFactory.create(file);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
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
    }
}
