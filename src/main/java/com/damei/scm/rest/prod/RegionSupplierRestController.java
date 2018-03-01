package com.damei.scm.rest.prod;

import com.google.common.collect.Lists;
import com.damei.scm.common.BaseComController;
import com.damei.scm.common.Constants;
import com.damei.scm.common.dto.MutipleDataStatusDto;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.utils.MapUtils;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.eum.AccoutTypeEnum;
import com.damei.scm.entity.eum.RegionSupplierEnum;
import com.damei.scm.entity.prod.RegionSupplier;
import com.damei.scm.entity.prod.Supplier;
import com.damei.scm.service.account.UserService;
import com.damei.scm.service.prod.RegionSupplierService;
import com.damei.scm.service.prod.SupplierService;
import com.damei.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/regionSupplier")
public class RegionSupplierRestController extends BaseComController<RegionSupplierService, RegionSupplier> {
	@Autowired
	private UserService userService;
	@Autowired
	private SupplierService supplierService;


	@RequestMapping("/list")
	public Object search(
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) String storeCode,
		@RequestParam(required = false) String status,
		@RequestParam(defaultValue = "0") int offset,
		@RequestParam(defaultValue = "20") int limit,
		@RequestParam(defaultValue = "id") String orderColumn,
		@RequestParam(defaultValue = "DESC") String orderSort) {
		Map<String, Object> params = new HashMap<String, Object>();
		ShiroUser shiroUser = WebUtils.getLoggedUser();

		MapUtils.putNotNull(params, "keyword", keyword);
		MapUtils.putNotNull(params, "storeCode", storeCode);
		MapUtils.putNotNull(params, "status", status);

		if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType())) {
			params.put("storeCode", shiroUser.getStoreCode());
		} else if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
			params.put("regionId", shiroUser.getSupplierId());
		}
		params.put(Constants.PAGE_OFFSET, offset);
		params.put(Constants.PAGE_SIZE, limit);
		params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public Object edit(RegionSupplier supplier) {
		if (StringUtils.isBlank(supplier.getName())) {
			return StatusDto.buildFailureStatusDto("区域供应商名称不能为空！");
		}

		if (supplier.getStore().getCode() == null) {
			return StatusDto.buildFailureStatusDto("必须选择所属门店");
		}

		if (service.checkNameExists(supplier)) {
			return StatusDto.buildFailureStatusDto("区域供应商名称已存在！");
		}

		boolean isCreate = supplier.getId() == null;
		supplier.setStatus(RegionSupplierEnum.OPEN);
		service.saveOrUpdate(supplier);

		if (isCreate) {
			userService.createSaveSupplierAccount(supplier.getId(), supplier.getName(), AccoutTypeEnum.REGION_SUPPLIER,null);
		}
		return StatusDto.buildSuccessStatusDto("操作成功！！！");
	}

	@RequestMapping(value = "/all")
	public Object findAll() {

		List<RegionSupplier> regionList = Collections.emptyList();

		ShiroUser shiroUser = WebUtils.getLoggedUser();
		if (shiroUser == null) {
			return StatusDto.buildDataSuccessStatusDto(regionList);
		}

		if (AccoutTypeEnum.ADMIN.equals(shiroUser.getAcctType())) {
			regionList = this.service.findAll();
		} else if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
			regionList = Lists.newArrayList(this.service.getById(shiroUser.getSupplierId()));
		} else if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType())) {
			regionList = this.service.findRegionSuppliersByStoreCode(shiroUser.getStoreCode());
		} else if (AccoutTypeEnum.PROD_SUPPLIER.equals(shiroUser.getAcctType())) {
			Supplier supplier = this.supplierService.getById(shiroUser.getSupplierId());
			regionList = Lists.newArrayList(this.service.getById(supplier.getRegionSupplier().getId()));
		}
		return StatusDto.buildDataSuccessStatusDto(regionList);
	}
	@RequestMapping(value = "/{regionSupplierId}/getWithOwnList", method = RequestMethod.GET)
	public Object getAndLoadOwnStoreList(@PathVariable Long regionSupplierId) {
		RegionSupplier regionSupplier = this.service.getById(regionSupplierId);
		List<RegionSupplier> regionSupplierList = null;
		if (regionSupplier != null) {
			regionSupplierList = this.service.findSameStoreIdRegionSuppliersById(regionSupplier.getId());
		}

		return MutipleDataStatusDto.buildMutipleDataSuccessDto().append("regionSupplier", regionSupplier)
			.append("regionSupplierList", regionSupplierList);
	}

	@RequestMapping(value = "/filterByStoreCode")
	public Object findListByStatus(@RequestParam(required = false) String storeCode) {
		List<RegionSupplier> regionList = Collections.emptyList();
		ShiroUser shiroUser = WebUtils.getLoggedUser();
		if (shiroUser == null) {
			return StatusDto.buildDataSuccessStatusDto(regionList);
		}
		if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
			regionList = Lists.newArrayList(this.service.getById(shiroUser.getSupplierId()));
		}else if (AccoutTypeEnum.PROD_SUPPLIER.equals(shiroUser.getAcctType())) {
			Supplier supplier = this.supplierService.getById(shiroUser.getSupplierId());
			regionList = Lists.newArrayList(this.service.getById(supplier.getRegionSupplier().getId()));
		}else{
			regionList = this.service.findRegionSuppliersByStoreCode(storeCode);
		}
		return StatusDto.buildDataSuccessStatusDto(regionList);
	}

	@RequestMapping(value = "/getByStoreCode/{storeCode}")
	public Object getByStoreCode(@PathVariable String storeCode) {
		List<RegionSupplier> regionList = Collections.emptyList();
		regionList = this.service.findRegionSuppliersByStoreCode(storeCode);
		return StatusDto.buildDataSuccessStatusDto(regionList);
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public Object changeStatus(@RequestParam Long regionSupplierId, @RequestParam RegionSupplierEnum newStatus) {
		RegionSupplier regionSupplier = new RegionSupplier();
		regionSupplier.setId(regionSupplierId);
		regionSupplier.setStatus(newStatus);
		service.updateStatus(regionSupplier);
		return StatusDto.buildSuccessStatusDto("操作成功！！！");
	}

}