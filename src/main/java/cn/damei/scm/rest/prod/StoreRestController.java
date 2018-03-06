package cn.damei.scm.rest.prod;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.Constants;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.entity.eum.AccoutTypeEnum;
import cn.damei.scm.entity.prod.RegionSupplier;
import cn.damei.scm.entity.prod.Store;
import cn.damei.scm.entity.prod.Supplier;
import cn.damei.scm.service.account.UserService;
import cn.damei.scm.service.prod.RegionSupplierService;
import cn.damei.scm.service.prod.SupplierService;
import cn.damei.scm.shiro.ShiroUser;
import com.google.common.collect.Lists;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.service.prod.StoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product/store")
public class StoreRestController extends BaseComController<StoreService, Store> {

	@Autowired
	private UserService userService;
	@Autowired
	private RegionSupplierService regionSupplierService;
	@Autowired
	private SupplierService supplierService;

	@RequestMapping("/list")
	public Object search(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int offset,
		@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "id") String orderColumn,
		@RequestParam(defaultValue = "DESC") String orderSort) {
		Map<String, Object> params = new HashMap<String, Object>();
		ShiroUser shiroUser = WebUtils.getLoggedUser();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType())) {
			params.put("storeCode", shiroUser.getSupplierId());
		}
		params.put(Constants.PAGE_OFFSET, offset);
		params.put(Constants.PAGE_SIZE, limit);
		params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
	}
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public Object edit(Store store) {
		if (StringUtils.isBlank(store.getName())) {
			return StatusDto.buildFailureStatusDto("门店名称不能为空！");
		}

		if (service.checkNameExists(store)) {
			return StatusDto.buildFailureStatusDto("门店名称已存在！");
		}

		boolean isCreate = store.getId() == null;
		service.saveOrUpdate(store);

		if (isCreate) {
			userService.createSaveSupplierAccount(store.getId(), store.getName(), AccoutTypeEnum.STORE,null);
		}
		return StatusDto.buildSuccessStatusDto("操作保存成功！！！");
	}

	@RequestMapping(value = "/all")
	public Object findAll() {
		List<Store> storeList = Collections.emptyList();

		ShiroUser shiroUser = WebUtils.getLoggedUser();
		if (shiroUser == null) {
			return StatusDto.buildDataSuccessStatusDto(storeList);
		}

		if (AccoutTypeEnum.ADMIN.equals(shiroUser.getAcctType())||AccoutTypeEnum.CHAIRMAN_FINANCE.equals(shiroUser.getAcctType())||AccoutTypeEnum.MATERIAL_MANAGER.equals(shiroUser.getAcctType())) {
			storeList = this.service.findAll();

		} else if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType())) {
			storeList = Lists.newArrayList(this.service.getByCode(shiroUser.getStoreCode()));
		}  else if (AccoutTypeEnum.MATERIAL_CLERK.equals(shiroUser.getAcctType())) {
			storeList = Lists.newArrayList(this.service.getByCode(shiroUser.getStoreCode()));
		} else if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
			RegionSupplier regionSupplier = this.regionSupplierService.getById(shiroUser.getSupplierId());
			storeList = Lists.newArrayList(this.service.getByCode(regionSupplier.getStore().getCode()));
		}else {
			Supplier supplier = this.supplierService.getById(shiroUser.getSupplierId());
			if(supplier==null){
				return StatusDto.buildDataSuccessStatusDto(storeList);
			}
			RegionSupplier regionSupplier = this.regionSupplierService.getById(supplier.getRegionSupplier().getId());
			storeList = Lists.newArrayList(this.service.getByCode(regionSupplier.getStore().getCode()));
		}
		return StatusDto.buildDataSuccessStatusDto(storeList);
	}
	@RequestMapping(value = "getFromDateCenter")
	public Object getFromDateCenter(){
		if( this.service.getFromDateCenter() ){
			return StatusDto.buildSuccessStatusDto("同步门店数据成功！");
		}else{
			return StatusDto.buildFailureStatusDto("同步门店数据失败！");
		}
	}
}
