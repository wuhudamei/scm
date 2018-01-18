package com.mdni.scm.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.service.BaseService;
import com.mdni.scm.common.utils.ReflectionUtils;

/**
 * Created by weiys on 16/4/15.
 */
public abstract class BaseComController<S extends BaseService<T>, T extends IdEntity> extends BaseController {
	//实体类的Class
	private final Class<T> entityClass = ReflectionUtils.getSuperClassGenricType(getClass(), 1);

	@Autowired
	protected S service;

	//查询
	@RequestMapping()
	public Object search(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int offset,
		@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "id") String orderColumn,
		@RequestParam(defaultValue = "DESC") String orderSort) {
		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		params.put(Constants.PAGE_OFFSET, offset);
		params.put(Constants.PAGE_SIZE, limit);
		params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
	}

	//增加或修改
	@RequestMapping(value = "/save")
	public Object saveOrUpdate(T entity) {
		if (entity.getId() != null && entity.getId() > 0) {
			service.update(entity);
		} else {
			service.insert(entity);
		}
		return StatusDto.buildSuccessStatusDto("保存成功！");
	}

	//删除
	@RequestMapping(value = "{id}/del")
	public Object delete(@PathVariable Long id) {
		service.deleteById(id);
		return StatusDto.buildSuccessStatusDto("删除操作成功！");
	}

	//获得详细
	@RequestMapping(value = "{id}")
	public Object get(@PathVariable Long id) {
		return StatusDto.buildDataSuccessStatusDto(service.getById(id));
	}
	
	//获得所有
	@RequestMapping(value = "/findAll")
	public Object findAll() {
		return StatusDto.buildDataSuccessStatusDto(service.findAll());
	}
}
