package com.damei.scm.rest.dataArrangement;

import com.google.common.collect.Maps;
import com.damei.scm.common.BaseComController;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.utils.MapUtils;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.dataArrangement.MaterialStandardConfig;
import com.damei.scm.service.dataArrangement.MaterialStandardConfigService;
import com.damei.scm.shiro.ShiroUser;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/material/standardConfig")
@SuppressWarnings("all")
public class MaterialStandardConfigController extends BaseComController<MaterialStandardConfigService,MaterialStandardConfig> {

    private final Integer delStatus = 1;
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Object save(@RequestBody MaterialStandardConfig materialHouseReform) {
        if (materialHouseReform.getId() != null) {
            service.update(materialHouseReform);
        } else {
            materialHouseReform.setCreateAccount(WebUtils.getLoggedUser().getLoginName());
            materialHouseReform.setCreateDate(new Date());
            materialHouseReform.setDelStatus(0);//删除的状态，默认为 0 ，没删除
            service.insert(materialHouseReform);
        }
        return StatusDto.buildDataSuccessStatusDto(materialHouseReform.getId());
    }

    @RequestMapping(value = "/findByContractId", method = RequestMethod.GET)
    public Object findByContractIdAndMetarialType(
            @RequestParam(required = true) Long contractId){
        return StatusDto.buildDataSuccessStatusDto(this.service.findByContractId(contractId));
    }

    @RequestMapping("/delete")
    public Object delete(@RequestParam(required = false) Long id){
        if (id == null) {
            return StatusDto.buildFailureStatusDto("记录不存在");
        }
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        String delAccount = loggedUser.getName();
        Date delDate = new Date();
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap,"delAccount",delAccount);
        MapUtils.putNotNull(paramMap,"delDate",delDate);
        MapUtils.putNotNull(paramMap,"delStatus",delStatus);
        MapUtils.putNotNull(paramMap,"id",id);
        this.service.delete(paramMap);
        return StatusDto.buildDataSuccessStatusDto("删除成功");
    }
}
