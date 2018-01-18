package com.mdni.scm.rest.prod;

import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.account.User;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Catalog;
import com.mdni.scm.service.prod.CatalogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * <dl>
 * <dd>描述: 商品类目RestController</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017-6-26 10:20:18</dd>
 * <dd>创建人： 张俊奎</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/catalog")
public class CatalogRestController extends BaseComController<CatalogService, Catalog> {

	@RequestMapping("list")
	public Object list(@RequestParam(required = false) String keyword,
		@RequestParam(required = false) StatusEnum status, @RequestParam(defaultValue = "0") int offset,
		@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "url") String orderColumn,
		@RequestParam(defaultValue = "ASC") String orderSort, @RequestParam(required = false) String parentId) {

		Map<String, Object> paramMap = Maps.newHashMap();
		MapUtils.putNotNull(paramMap, "keyword", keyword);
		MapUtils.putNotNull(paramMap, "status", status);
		MapUtils.putNotNull(paramMap, "parentId", parentId);
		paramMap.put(Constants.PAGE_OFFSET, offset);
		paramMap.put(Constants.PAGE_SIZE, limit);
		paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		BootstrapPage<Catalog> page = service.searchScrollPage(paramMap);

		return StatusDto.buildDataSuccessStatusDto(page);
	}

	/**
	 * 保存或更新
	 * 
	 * @param catalog 类目对象
	 * @param parentUrl 父Url
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public Object saveOrUpdate(Catalog catalog, @RequestParam(required = false) String parentUrl) {

		if (WebUtils.getLoggedUser() == null) {
			return StatusDto.buildFailureStatusDto("请先登录");
		}

		if (StringUtils.isBlank(catalog.getName())) {
			return StatusDto.buildFailureStatusDto("类目名称不能为空！");
		}

		if (this.service.isNameExists(catalog)) {
			return StatusDto.buildFailureStatusDto("类目名称已存在！");
		}

		//当parentUrl 不为null或不为空串时,表示当前不是顶层父类目
		Catalog parent = new Catalog();
		if (StringUtils.isNotBlank(parentUrl)) {
			//传递过来的就是其父url
			parent.setUrl(parentUrl);
		} else {
			//当前是顶层父类目,将其parentId设置为0
			parent.setId(0L);
		}
		catalog.setParent(parent);

		service.saveOrUpdate(buildDetail(catalog));
		return StatusDto.buildSuccessStatusDto("操作成功！");
	}

	/**
	 * 修改状态:启用/停用
	 */
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public Object changeStatus(@RequestParam Long id, @RequestParam StatusEnum status) {
		Catalog byId = service.getById(id);
		if(byId==null){
			StatusDto.buildFailureStatusDto("类目不存在");
		}
		Catalog catalog = new Catalog();
		catalog.setId(id);
		catalog.setStatus(status);
		catalog.setUrl(byId.getUrl());
		if(StatusEnum.OPEN.equals(status)){
			service.open(catalog);
		}else{
			service.lock(catalog);
		}
		return StatusDto.buildSuccessStatusDto("操作成功！");
	}

	/**
	 * 查找所有类目
	 */
	@RequestMapping("/findCatalogList")
	public Object findAll() {
		return StatusDto.buildDataSuccessStatusDto(service.buildCatalogTree());
	}

	@RequestMapping("/getByUrl")
	public Object getByUrl(@RequestParam String catalogUrl){
		return StatusDto.buildDataSuccessStatusDto(this.service.getByUrl(catalogUrl));
	}

	/**
	 * 查询可以复尺的类目
	 * @return
	 */
	@RequestMapping("/findCatalogByIsReject")
	public Object findCatalogByIsReject(){
		List<Catalog> catalog = this.service.findCatalogByIsReject();
		return StatusDto.buildDataSuccessStatusDto(catalog);
	}

	private Catalog buildDetail(Catalog catalog) {
		catalog.setEditor(new User(WebUtils.getLoggedUserId()));
		catalog.setEditTime(new Date());
		return catalog;
	}

	@RequestMapping("/findCatalogParent")
	public Object findCatalogParent(@RequestParam(required = true) String parentId){
		return StatusDto.buildDataSuccessStatusDto(this.service.findCatalogParent(Long.valueOf(parentId)));
	}
}