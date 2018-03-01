package com.damei.scm.rest.prod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.damei.scm.common.BaseComController;
import com.damei.scm.common.Constants;
import com.damei.scm.common.PropertyHolder;
import com.damei.scm.common.dto.BootstrapPage;
import com.damei.scm.common.dto.MutipleDataStatusDto;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.utils.MapUtils;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.eum.AccoutTypeEnum;
import com.damei.scm.entity.eum.StatusEnum;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/supplier")
public class SupplierRestController extends BaseComController<SupplierService, Supplier> {
	@Autowired
	private RegionSupplierService regionSupplierService;
	@Autowired
	private UserService userService;

	@RequestMapping("list")
	public Object list(@RequestParam(required = false) String keyword,
		@RequestParam(required = false) StatusEnum status, @RequestParam(required = false) Long regionSupplierId,
		@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit,
		@RequestParam(defaultValue = "id") String orderColumn, @RequestParam(defaultValue = "DESC") String orderSort) {

		List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();
		if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
			return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
		}

		Map<String, Object> params = Maps.newHashMap();
		MapUtils.putNotNull(params, "keyword", keyword);
		MapUtils.putNotNull(params, "status", status);
		MapUtils.putNotNull(params,"regionSupplierId",regionSupplierId);
		MapUtils.putNotNull(params, "managedSupplierIdList", managedSupplierIdList);

		params.put(Constants.PAGE_OFFSET, offset);
		params.put(Constants.PAGE_SIZE, limit);
		params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		BootstrapPage<Supplier> page = super.service.searchScrollPage(params);
		return StatusDto.buildDataSuccessStatusDto(page);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public Object edit(Supplier supplier) {
		if (StringUtils.isBlank(supplier.getName())) {
			return StatusDto.buildFailureStatusDto("供应商名称不能为空！");
		}
		if (StringUtils.isAnyBlank(supplier.getContactor(), supplier.getMobile(), supplier.getAddress())) {
			return StatusDto.buildFailureStatusDto("供应商联系人信息、公司地址不能为空！");
		}
		if (service.isCodeExist(supplier)) {
			return StatusDto.buildFailureStatusDto("供应商编码已经存在！");
		}

		if (service.getByName(supplier) != null) {
			return StatusDto.buildFailureStatusDto("供应商名称已经存在！");
		}

		boolean isCreate = supplier.getId() == null;

		supplier.setEditor(WebUtils.getLoggedUser().valueOf());
		supplier.setEditTime(new Date());
		service.saveOrUpdate(supplier);

		if (isCreate) {
			userService.createSaveSupplierAccount(supplier.getId(), supplier.getName(), AccoutTypeEnum.PROD_SUPPLIER,supplier.getPhone());
		}
		return StatusDto.buildSuccessStatusDto("操作成功！！！");
	}

	@RequestMapping(value = "/getById/{id}",method = RequestMethod.GET)
	public Object getById(@PathVariable Long id){
		Supplier supplier = service.getById(id);
		if( StringUtils.isNotBlank( supplier.getBusinessLicenseImageUrl() ) ){
			supplier.setBusinessLicenseImageFullUrl( PropertyHolder.getImageBaseUrl() + supplier.getBusinessLicenseImageUrl() );
		}
		if( StringUtils.isNotBlank( supplier.getTaxRegistrationCertificateImageUrl() ) ){
			supplier.setTaxRegistrationCertificateImageFullUrl( PropertyHolder.getImageBaseUrl() + supplier.getTaxRegistrationCertificateImageUrl() );
		}
		return StatusDto.buildDataSuccessStatusDto(supplier);
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public Object changeStatus(@RequestParam Long supplierId, @RequestParam StatusEnum newStatus) {
		Supplier supplier = new Supplier();
		supplier.setId(supplierId);
		supplier.setStatus(newStatus);
		service.update(supplier);
		return StatusDto.buildSuccessStatusDto("操作成功！！！");
	}

	@RequestMapping(value = "/{supplierId}/getWithOwnList", method = RequestMethod.GET)
	public Object getAndLoadOwnRegionSupplierList(@PathVariable Long supplierId) {
		Supplier supplier = this.service.getById(supplierId);
		supplier.setRegionSupplier(this.regionSupplierService.getById(supplier.getRegionSupplier().getId()));
		List<Supplier> supplierList = null;
		if (supplier != null) {
			supplierList = this.service.findSuppliersByRegionIdAndStatus(supplier.getRegionSupplier().getId(), null);
		}

		return MutipleDataStatusDto.buildMutipleDataSuccessDto().append("supplier", supplier)
			.append("supplierList", supplierList);
	}

	@RequestMapping(value = "/filterByRegionIdAndStatus")
	public Object findListByStatus(@RequestParam(required = false) Long regionSupplierId,
		@RequestParam(required = false) StatusEnum status) {
		List<Supplier> supplierList = Collections.emptyList();
		ShiroUser shiroUser = WebUtils.getLoggedUser();
		if (shiroUser == null) {
			return StatusDto.buildDataSuccessStatusDto(supplierList);
		}
		if (AccoutTypeEnum.PROD_SUPPLIER.equals(shiroUser.getAcctType())) {
			supplierList = Lists.newArrayList(this.service.getById(shiroUser.getSupplierId()));
		}else{
			supplierList = this.service.findSuppliersByRegionIdAndStatus(regionSupplierId, status);
		}
		return StatusDto.buildDataSuccessStatusDto(supplierList);
	}

	@RequestMapping(value = "/findall")
	public Object findAllSupplier(){
		List<Supplier> supplierList = this.service.findAll();
		if(supplierList == null){
			return StatusDto.buildFailureStatusDto("供应商不存在");
		}
		return StatusDto.buildDataSuccessStatusDto(supplierList);
	}

	@RequestMapping(value = "/findByName", method = RequestMethod.GET)
	public Object getAllSupplier(@RequestParam(required = false) String name) {
		Map<String, Object> params = Maps.newHashMap();
		MapUtils.putNotNull(params, "storeCode", WebUtils.getLoggedUser().getStoreCode());
		MapUtils.putNotNull(params, "name",name);
		return MutipleDataStatusDto.buildMutipleDataSuccessDto().append("rows",service.findByMap(params));
	}

	@RequestMapping(value = "/getBySupplierId")
	public Object getBySupplierId(@RequestParam(required = false) Long supplierId) {
		List<Supplier> supplierList = this.service.findBySupplierId(supplierId);

		if( supplierList != null && supplierList.size() > 0 ){
			return MutipleDataStatusDto.buildMutipleDataSuccessDto()
					.append("regionSupplierId", supplierList.get(0).getRegionSupplier().getId())
					.append("supplierList", supplierList)
					.append("regionSupplierList",this.regionSupplierService.findSameStoreIdRegionSuppliersById(supplierList.get(0).getRegionSupplier().getId()));
		}else{
			return MutipleDataStatusDto.buildMutipleDataSuccessDto()
					.append("regionSupplierId", null)
					.append("supplierList", Collections.emptyList())
					.append("regionSupplierList", Collections.emptyList());
		}
	}
}