package com.mdni.scm.rest.prepareorder;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.prepareorder.IndentPrepareOrder;
import com.mdni.scm.service.prepareorder.IndentPrepareOrderService;
import com.mdni.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @Description: 订货预备单controller
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 19:01.
 */
@RestController
@RequestMapping("/material/prepareorder")
public class IndentPrepareOrderController extends BaseComController<IndentPrepareOrderService, IndentPrepareOrder> {

    @Autowired
    private IndentPrepareOrderService indentPrepareOrderService;

    /**
     * 预备单 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String dataSource,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) Long brandId,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate,
                       @RequestParam(defaultValue = "0") int offset,
                       @RequestParam(defaultValue = "20") int limit,
                       @RequestParam(defaultValue = "id") String orderColumn,
                       @RequestParam(defaultValue = "DESC") String orderSort) {

        ShiroUser loginUser = WebUtils.getLoggedUser();
        AccoutTypeEnum acctType1 = loginUser.getAcctType();
        if(acctType1==null){
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "status", status);
        MapUtils.putNotNull(paramMap, "brandId", brandId);
        MapUtils.putNotNull(paramMap, "startDate", startDate);
        MapUtils.putNotNull(paramMap, "endDate", endDate);

        //由于"select"关键字不能传递,故使用0
        if("0".equals(dataSource)){
            paramMap.put("dataSource", Constants.DATA_SOURCE_SELECT);
        }else if(StringUtils.isNotBlank(dataSource)){
            MapUtils.putNotNull(paramMap, "dataSource", dataSource);
        }
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(indentPrepareOrderService.searchScrollPage(paramMap));
    }

    /**
     * 转订货单
     */
    @RequestMapping(value = "/turntoindentorder")
    public Object turnToIndentOrder(Long id){
        if(id == null){
            return StatusDto.buildFailureStatusDto("参数id不能为空!");
        }
        return indentPrepareOrderService.turnToIndentOrder(id);
    }

    /**
     *  新增或修改
     * @param entity
     * @return
     */
    @Override
    @RequestMapping(value = "/save")
    public Object saveOrUpdate(IndentPrepareOrder entity) {
        Date now = new Date();
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if (loggedUser == null) {
            return StatusDto.buildFailureStatusDto("回话失效,请重新登录!");
        }
        String accoutName = loggedUser.getName() + "(" + loggedUser.getLoginName() + ")";
        if (entity.getId() != null && entity.getId() > 0) {
            entity.setUpdateTime(now);
            entity.setUpdateAccount(accoutName);
            indentPrepareOrderService.update(entity);
        } else {
            entity.setCreateTime(now);
            indentPrepareOrderService.insert(entity);
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    @RequestMapping("/finddetail")
    public Object findDetail(Long prepareOrderId){
        return StatusDto.buildDataSuccessStatusDto(this.indentPrepareOrderService.getByPrepareOrderId(prepareOrderId));
    }

}
