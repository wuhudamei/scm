package com.mdni.scm.rest.dataArrangement;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.dataArrangement.MaterialHouseReform;
import com.mdni.scm.service.dataArrangement.MaterialHouseReformService;
import com.mdni.scm.shiro.ShiroUser;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你 房屋拆改controller</dd>
 * <dd>@date：2017/8/4  18:21</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@RestController
@RequestMapping("/api/material/houseReform")
@SuppressWarnings("all")
public class MaterialHouseReformController extends BaseComController<MaterialHouseReformService,MaterialHouseReform> {
    private final Integer delStatus = 1;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Object save(@RequestBody MaterialHouseReform materialHouseReform) {
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

    /**
     *根据合同id查询对应的材料列表
     * @param contractId
     * @return
     */
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
