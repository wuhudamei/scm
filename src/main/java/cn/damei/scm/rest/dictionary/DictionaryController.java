package cn.damei.scm.rest.dictionary;

import cn.damei.scm.common.Constants;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.entity.dictionary.Dictionary;
import cn.damei.scm.service.dictionary.DictionaryService;
import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/system/dictionary")
@SuppressWarnings("all")
public class DictionaryController extends BaseComController<DictionaryService, Dictionary> {
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
    @RequestMapping(value = "{id}")
    public Object get(@PathVariable Long id) {
        Dictionary byId = service.getById(id);
        if(byId!=null){
            byId.setList(service.findByParentId(id));
        }
        return StatusDto.buildDataSuccessStatusDto(byId);
    }
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

    @RequestMapping(value = "/findByValue",method = RequestMethod.GET)
    public Object findByValue(String dicValue){
        return StatusDto.buildDataSuccessStatusDto(service.findByParentValue(dicValue)) ;
    }


}
