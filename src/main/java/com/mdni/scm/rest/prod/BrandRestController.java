package com.mdni.scm.rest.prod;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.MutipleDataStatusDto;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.PingYinUtil;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Brand;
import com.mdni.scm.service.prod.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * <dl>
 * <dd>描述: 品牌RestController</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 下午5:08:10</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/brand")
public class BrandRestController extends BaseComController<BrandService, Brand> {

	/**
	 * @param keyword 代码/名字/名字拼音首字母
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Object list(@RequestParam(required = false) String keyword,
		@RequestParam(required = false) StatusEnum status, @RequestParam(defaultValue = "0") int offset,
		@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "id") String orderColumn,
		@RequestParam(defaultValue = "ASC") String orderSort) {

		Map<String, Object> paramMap = Maps.newHashMap();
		MapUtils.putNotNull(paramMap, "keyword", keyword);
		MapUtils.putNotNull(paramMap, "status", status);
		paramMap.put(Constants.PAGE_OFFSET, offset);
		paramMap.put(Constants.PAGE_SIZE, limit);
		paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort), orderColumn));
		return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(paramMap));
	}

	//增加或修改
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public Object saveOrUpdate(Brand brand) {
		if (WebUtils.getLoggedUserId() == null) {
			return StatusDto.buildFailureStatusDto("请选登录");
		}

		if (StringUtils.isAnyBlank(brand.getCode(), brand.getBrandName())) {
			return StatusDto.buildFailureStatusDto("品牌编码、名称都不能为空！");
		}

		if (service.isCodeExists(brand)) {
			return StatusDto.buildFailureStatusDto("品牌编码已存在！");
		}

		if (service.isNameExists(brand)) {
			return StatusDto.buildFailureStatusDto("品牌名称已存在！");
		}

		service.saveOrUpdate(buildDetail(brand));
		return StatusDto.buildSuccessStatusDto("品牌保存成功");
	}

	//启用或停用
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public Object changeStatus(@RequestParam Long id, @RequestParam StatusEnum status) {
		service.changeStatus(id, status);
		return StatusDto.buildSuccessStatusDto("品牌状态修改成功！");
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Object findAllOpenBrand() {
		return StatusDto.buildDataSuccessStatusDto(service.findAllForMap());
	}

	/**
	 *获取品牌数组
	 * @return
	 */
	/*@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public Object getAllOpenBrand() {
		return MutipleDataStatusDto.buildMutipleDataSuccessDto().append("rows",service.findAllForMap());
	}*/
	
	/**
	 *获取供应商数组
	 * @return
	 */
	@RequestMapping(value = "/findByName", method = RequestMethod.GET)
	public Object getAllSupplier(@RequestParam(required = false) String name) {
		return MutipleDataStatusDto.buildMutipleDataSuccessDto().append("rows",service.findByName(name));
	}

	public Brand buildDetail(Brand brand) {
		brand.setPinyinInitial(PingYinUtil.getFirstSpell(brand.getBrandName()));
		brand.setEditor(WebUtils.getLoggedUser().valueOf());
		brand.setEditTime(new Date());
		return brand;
	}


}