package cn.damei.scm.rest.prod;

import java.util.Date;
import java.util.List;

import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.entity.eum.PriceTypeEnum;
import cn.damei.scm.entity.prod.SkuPrice;
import cn.damei.scm.service.prod.SkuPriceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.dto.BootstrapPage;
import cn.damei.scm.common.utils.WebUtils;

@RestController
@RequestMapping(value = "/api/product/price")
public class SkuPriceRestController extends BaseComController<SkuPriceService, SkuPrice> {

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Object list(@RequestParam Long skuId, @RequestParam PriceTypeEnum priceType) {

        List<SkuPrice> priceList = service.findBySkuIdAndType(skuId, priceType);
        return StatusDto.buildDataSuccessStatusDto(new BootstrapPage<SkuPrice>(Long.valueOf(priceList.size()),
                priceList));
    }
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Object saveOrUpdate(SkuPrice price) {

        if (price.getPriceType() == null) {
            return StatusDto.buildFailureStatusDto("价格类型不能为空");
        }

        if (price.getPriceStartDate() == null) {
            return StatusDto.buildFailureStatusDto("开始计价日期不能为空");
        }

        SkuPrice skuPrice = service.getByDate(price);
        if(skuPrice != null){
            return StatusDto.buildFailureStatusDto("同一天不能设置两次价格");
        }
        price.setEditor(WebUtils.getLoggedUser().valueOf());
        price.setEditTime(new Date());
        service.saveOrUpdate(price);
        return StatusDto.buildSuccessStatusDto("价格编辑成功");
    }

}