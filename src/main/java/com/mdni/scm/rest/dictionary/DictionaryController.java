package com.mdni.scm.rest.dictionary;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.dictionary.Dictionary;
import com.mdni.scm.service.dictionary.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你scm  字典 controller</dd>
 * <dd>@date：2017/8/11  11:30</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */

@RestController
@RequestMapping("/api/system/dictionary")
@SuppressWarnings("all")
public class DictionaryController extends BaseComController<DictionaryService, Dictionary> {
    //查询 列表
    @RequestMapping()
    public Object search(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "sort") String orderColumn,
                         @RequestParam(defaultValue = "ASC") String orderSort) {
        Map<String, Object> params = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
    }
    //获得详细
    @RequestMapping(value = "{id}")
    public Object get(@PathVariable Long id) {
        Dictionary byId = service.getById(id);
        if(byId!=null){
            byId.setList(service.findByParentId(id));
        }
        return StatusDto.buildDataSuccessStatusDto(byId);
    }
    //增加或修改
    @RequestMapping(value = "/save")
    public Object saveOrUpdate(@RequestBody Dictionary entity) {

        if (entity.getId() != null && entity.getId() > 0) {
            service.update(entity);
        } else {
            entity.setCreateAccount(WebUtils.getLoggedUser().getLoginName());
            entity.setCreateTime(new Date());
            service.insert(entity);
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    /**
     *  根据 字典 父类值 获取 子节点 列表 下拉框
     * @param dicValue
     * @return
     */
    @RequestMapping(value = "/findByValue",method = RequestMethod.GET)
    public Object findByValue(String dicValue){
        return StatusDto.buildDataSuccessStatusDto(service.findByParentValue(dicValue)) ;
    }


}
