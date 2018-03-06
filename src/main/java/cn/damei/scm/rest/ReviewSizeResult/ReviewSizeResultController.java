package cn.damei.scm.rest.ReviewSizeResult;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.Constants;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.MapUtils;
import cn.damei.scm.entity.eum.ReviewSizeNoticeEnum;
import cn.damei.scm.entity.prod.Catalog;
import cn.damei.scm.entity.reviewSizeNotice.ReviewSizeNotice;
import cn.damei.scm.entity.reviewSizeResult.ReviewSizeResult;
import cn.damei.scm.service.customer.CustomerContractService;
import cn.damei.scm.service.operatorLog.OperateLogService;
import cn.damei.scm.service.prod.BrandService;
import cn.damei.scm.service.prod.CatalogService;
import cn.damei.scm.service.reviewSizeNotice.ReviewSizeNoticeService;
import com.google.common.collect.Maps;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.service.ReviewSizeResultService.ReviewSizeResultService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/reviewSizeResult")
public class ReviewSizeResultController extends BaseComController<ReviewSizeResultService, ReviewSizeResult> {

    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private ReviewSizeNoticeService reviewSizeNoticeService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private CatalogService catalogService;

    @RequestMapping("/list")
    public Object list(@RequestParam(required = true) String contractId,
                       @RequestParam(required = true) String prodCatalogId,
                       @RequestParam(defaultValue = "0") int offset,
                       @RequestParam(defaultValue = "20") int limit){
        if(contractId == null){
            return StatusDto.buildFailureStatusDto("项目不存在");
        }
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "contractId", contractId);

        if(StringUtils.isNotBlank( prodCatalogId ) ){
            Catalog catalog = catalogService.getById( Long.parseLong( prodCatalogId ) );
            String[] url_array =  catalog.getUrl().split("-");
            MapUtils.putNotNull(paramMap, "prodCatalogId", url_array[0] + "-%");
        }
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
    }

    @RequestMapping("/insert")
    public Object insert(@RequestBody List<ReviewSizeResult> list){
        if(list == null){
            return StatusDto.buildFailureStatusDto("复尺记录为空，保存失败！");
        }
        String name = WebUtils.getLoggedUser().getName();
        Date date = new Date();
        for(ReviewSizeResult reviewSizeResult : list){
            reviewSizeResult.setCreater(name);
            reviewSizeResult.setCreateTime(date);
            reviewSizeResult.setContractId(reviewSizeResult.getContractId());
            this.service.insert(reviewSizeResult);
            reviewSizeNoticeService.updateReviewStatus(reviewSizeResult.getReviewSizeNoticeId(), ReviewSizeNoticeEnum.YESRIVEEWSIZE);
        }
        return StatusDto.buildDataSuccessStatusDto("保存成功！");
    }

    @RequestMapping(value = "/getReviewSizeResultById",method = RequestMethod.GET)
    public Object getReviewSizeResultById(@RequestParam(required = false) Long id){
        if(id == null){
            return StatusDto.buildFailureStatusDto("该复尺结果不存在！");
        }
        List<ReviewSizeResult> reviewSizeResultList = this.service.getReviewSizeResultById(id);
        for(ReviewSizeResult reviewSizeResult : reviewSizeResultList){
            Catalog catalog = catalogService.getById(reviewSizeResult.getProdCatalogId());
            reviewSizeResult.setProdCatalogName(catalog.getName());
        }
        return StatusDto.buildDataSuccessStatusDto(reviewSizeResultList);
    }

    @RequestMapping("/delete")
    public Object delete(@RequestParam(required = false) Long id){
        if(id == null){
            return StatusDto.buildFailureStatusDto("该复尺记录不存在！");
        }
        this.service.deleteById(id);
        return StatusDto.buildDataSuccessStatusDto("删除复尺记录成功！");
    }

    @RequestMapping("/getById")
    public Object getById(@RequestParam Long id){
        if(id == null){
            return StatusDto.buildDataSuccessStatusDto("该复尺记录不存在！");
        }
        ReviewSizeResult reviewSizeResult = this.service.getById(id);
        return  StatusDto.buildDataSuccessStatusDto(reviewSizeResult);
    }

    @RequestMapping("/update")
    public Object update(@RequestBody List<ReviewSizeResult> list){
        if(list == null){
            return StatusDto.buildFailureStatusDto("复尺记录为空，保存失败！");
        }
        for(ReviewSizeResult reviewSizeResult : list){
            this.service.update(reviewSizeResult);
        }
        return StatusDto.buildDataSuccessStatusDto("保存成功！");
    }

    @RequestMapping("/getContract")
    public Object getContract(@RequestParam(required = false) Long contractId,
                              @RequestParam(required = false) Long id){
        ReviewSizeNotice reviewSizeNotice = reviewSizeNoticeService.getContract(contractId.toString(),id.toString());
        String contractCode = customerContractService.getContractCodeById(reviewSizeNotice.getContractId());//根据项目id查找项目编号
        String brandName = brandService.getBrandNameById(reviewSizeNotice.getBrandId());
        reviewSizeNotice.setSupplierId(contractCode);
        reviewSizeNotice.setBrandId(brandName);
        return StatusDto.buildDataSuccessStatusDto(reviewSizeNotice);
    }

}
