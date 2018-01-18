package com.mdni.scm.rest.prod;

import com.google.common.collect.Lists;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.MutipleDataStatusDto;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.eum.RegionSupplierEnum;
import com.mdni.scm.entity.prod.RegionSupplier;
import com.mdni.scm.entity.prod.Supplier;
import com.mdni.scm.service.account.UserService;
import com.mdni.scm.service.prod.RegionSupplierService;
import com.mdni.scm.service.prod.SupplierService;
import com.mdni.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <dl>
 * <dd>描述: 区域供应商</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月15日 下午1:13:51</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/regionSupplier")
public class RegionSupplierRestController extends BaseComController<RegionSupplierService, RegionSupplier> {
	@Autowired
	private UserService userService;
	@Autowired
	private SupplierService supplierService;

	// 查询
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

		// 如果是门店管理员，只能看到自己的门店下的区域供应商
		if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType())) {
			params.put("storeCode", shiroUser.getStoreCode());
		} else if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
			// 如果是区域供应商只能看见自己
			params.put("regionId", shiroUser.getSupplierId());
		}
		params.put(Constants.PAGE_OFFSET, offset);
		params.put(Constants.PAGE_SIZE, limit);
		params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
	}

	// 增加或修改
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
			// 创建区域供应商对应的账号：
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
			// 管理员可以选择所有的区域供应商
			regionList = this.service.findAll();
		} else if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
			// 区域供应商只能选择自己
			regionList = Lists.newArrayList(this.service.getById(shiroUser.getSupplierId()));
		} else if (AccoutTypeEnum.STORE.equals(shiroUser.getAcctType())) {
			// 门店可以选择这个门店下的所有供应商
			regionList = this.service.findRegionSuppliersByStoreCode(shiroUser.getStoreCode());
		} else if (AccoutTypeEnum.PROD_SUPPLIER.equals(shiroUser.getAcctType())) {
			Supplier supplier = this.supplierService.getById(shiroUser.getSupplierId());
			//商品供应商只能选择所属的区域供应商
			regionList = Lists.newArrayList(this.service.getById(supplier.getRegionSupplier().getId()));
		}
		return StatusDto.buildDataSuccessStatusDto(regionList);
	}

	// 获得区域供应商并且加载所属门店
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

	/**
	 * 查找区域供应商列表
	 */
	@RequestMapping(value = "/filterByStoreCode")
	public Object findListByStatus(@RequestParam(required = false) String storeCode) {
		List<RegionSupplier> regionList = Collections.emptyList();
		ShiroUser shiroUser = WebUtils.getLoggedUser();
		if (shiroUser == null) {
			return StatusDto.buildDataSuccessStatusDto(regionList);
		}
		if (AccoutTypeEnum.REGION_SUPPLIER.equals(shiroUser.getAcctType())) {
			// 区域供应商只能选择自己
			regionList = Lists.newArrayList(this.service.getById(shiroUser.getSupplierId()));
		}else if (AccoutTypeEnum.PROD_SUPPLIER.equals(shiroUser.getAcctType())) {
			Supplier supplier = this.supplierService.getById(shiroUser.getSupplierId());
			//商品供应商只能选择所属的区域供应商
			regionList = Lists.newArrayList(this.service.getById(supplier.getRegionSupplier().getId()));
		}else{
			regionList = this.service.findRegionSuppliersByStoreCode(storeCode);
		}
		return StatusDto.buildDataSuccessStatusDto(regionList);
	}

	/**
	 * 根据门店id获取区域goon供应商列表
	 * @param storeCode
	 * @return
	 */
	@RequestMapping(value = "/getByStoreCode/{storeCode}")
	public Object getByStoreCode(@PathVariable String storeCode) {
		List<RegionSupplier> regionList = Collections.emptyList();
		regionList = this.service.findRegionSuppliersByStoreCode(storeCode);
		return StatusDto.buildDataSuccessStatusDto(regionList);
	}

	//启用或停用
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public Object changeStatus(@RequestParam Long regionSupplierId, @RequestParam RegionSupplierEnum newStatus) {
		RegionSupplier regionSupplier = new RegionSupplier();
		regionSupplier.setId(regionSupplierId);
		regionSupplier.setStatus(newStatus);
		service.updateStatus(regionSupplier);
		return StatusDto.buildSuccessStatusDto("操作成功！！！");
	}

}