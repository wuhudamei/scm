package com.mdni.scm.rest.prod;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.PriceTypeEnum;
import com.mdni.scm.entity.prod.SkuPrice;
import com.mdni.scm.service.prod.SkuPriceService;

/**
 * <dl>
 * <dd>描述: 商品价格Controller</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 下午5:08:10</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/product/price")
public class SkuPriceRestController extends BaseComController<SkuPriceService, SkuPrice> {

    //返回 某个sku或主商品的 价格历史记录
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Object list(@RequestParam Long skuId, @RequestParam PriceTypeEnum priceType) {
        //商品供货商： 供货价（维护）
        //区域供应商：供货价（查看）,门店价（维护）
        //门店：门店(查看),销售价(维护)
        //管理员能看见所有的价格和维护价格

        List<SkuPrice> priceList = service.findBySkuIdAndType(skuId, priceType);
        return StatusDto.buildDataSuccessStatusDto(new BootstrapPage<SkuPrice>(Long.valueOf(priceList.size()),
                priceList));
    }

    //增加或修改
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