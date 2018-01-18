package com.mdni.scm.rest.prod;

import com.google.common.collect.Lists;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.prod.RegionSupplier;
import com.mdni.scm.entity.prod.Store;
import com.mdni.scm.entity.prod.Supplier;
import com.mdni.scm.service.account.UserService;
import com.mdni.scm.service.prod.RegionSupplierService;
import com.mdni.scm.service.prod.StoreService;
import com.mdni.scm.service.prod.SupplierService;
import com.mdni.scm.shiro.ShiroUser;
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

/**
 *
 * 大诚若谷信息技术有限公司 功能：门店Controller 作者:张俊奎 时间：2017年7月5日上午11:02:38
 */
@RestController
@RequestMapping("/api/product/store")
public class StoreRestController extends BaseComController<StoreService, Store> {

	@Autowired
	private UserService userService;
	@Autowired
	private RegionSupplierService regionSupplierService;
	@Autowired
	private SupplierService supplierService;

	// 查询
	@RequestMapping("/list")
	public Object search(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int offset,
		@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "id") String orderColumn,
		@RequestParam(defaultValue = "DESC") String orderSort) {
		Map<String, Object> params = new HashMap<String, Object>();
		ShiroUser shiroUser = WebUtils.getLoggedUser();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		// 如果是门店管理员，只能看到自己的门店
		if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType())) {
			params.put("storeCode", shiroUser.getSupplierId());
		}
		params.put(Constants.PAGE_OFFSET, offset);
		params.put(Constants.PAGE_SIZE, limit);
		params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
	}

	// 增加或修改
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
			// 创建区域供应商对应的账号：
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
			// 管理员可以选择所有的门店
			storeList = this.service.findAll();

		} else if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType())) {
			// 门店管理员只能选择自己
			storeList = Lists.newArrayList(this.service.getByCode(shiroUser.getStoreCode()));
		}  else if (AccoutTypeEnum.MATERIAL_CLERK.equals(shiroUser.getAcctType())) {
			//材料下单员
			storeList = Lists.newArrayList(this.service.getByCode(shiroUser.getStoreCode()));
		} else if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
			// 区域供应商只能选择自己所属的门店
			RegionSupplier regionSupplier = this.regionSupplierService.getById(shiroUser.getSupplierId());
			storeList = Lists.newArrayList(this.service.getByCode(regionSupplier.getStore().getCode()));
		}else {
			//商品供应商只能选择自己所属区域供应商的所属门店
			Supplier supplier = this.supplierService.getById(shiroUser.getSupplierId());
			//空指针处理
			if(supplier==null){
				return StatusDto.buildDataSuccessStatusDto(storeList);
			}
			RegionSupplier regionSupplier = this.regionSupplierService.getById(supplier.getRegionSupplier().getId());
			storeList = Lists.newArrayList(this.service.getByCode(regionSupplier.getStore().getCode()));
		}
		return StatusDto.buildDataSuccessStatusDto(storeList);
	}
	/**
	 * 同步综管系统门店数据
	 */
	@RequestMapping(value = "getFromDateCenter")
	public Object getFromDateCenter(){
		if( this.service.getFromDateCenter() ){
			return StatusDto.buildSuccessStatusDto("同步门店数据成功！");
		}else{
			return StatusDto.buildFailureStatusDto("同步门店数据失败！");
		}
	}
}
