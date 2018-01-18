package com.mdni.scm.rest.account;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.MapUtils;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.account.User;
import com.mdni.scm.entity.eum.AccoutTypeEnum;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.RegionSupplier;
import com.mdni.scm.entity.prod.Supplier;
import com.mdni.scm.service.account.RoleService;
import com.mdni.scm.service.account.UserService;
import com.mdni.scm.service.prod.RegionSupplierService;
import com.mdni.scm.service.prod.StoreService;
import com.mdni.scm.service.prod.SupplierService;
import com.mdni.scm.shiro.PasswordUtil;
import com.mdni.scm.shiro.ShiroUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 
 * <dl>
 * <dd>描述: 账户</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月9日 下午3:55:24</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserRestController extends BaseComController<UserService, User> {
	@Autowired
	private RoleService roleService;
	@Autowired
	private RegionSupplierService regionSupplierService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private StoreService stroeService;

	/**
	 * 获取登录用户类型
	 * @return
	 */
	@RequestMapping(value = "getLoginUserAccType",method = RequestMethod.GET)
	public Object getLoginUserAccType(){
		ShiroUser shiroUser = WebUtils.getLoggedUser();
		if( shiroUser != null && shiroUser.getAcctType() != null ){
			return StatusDto.buildDataSuccessStatusDto( shiroUser.getAcctType() );
		}else{
			return StatusDto.buildFailureStatusDto("获取登录用户账户类型失败");
		}
	}

	@RequestMapping("list")
	public Object list(@RequestParam(required = false) String keyword,
		@RequestParam(required = false) StatusEnum status,
		@RequestParam(required = false) AccoutTypeEnum acctType,
		@RequestParam(required = false) String storeCode,
		@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit,
		@RequestParam(defaultValue = "id") String orderColumn, @RequestParam(defaultValue = "DESC") String orderSort) {

		Map<String, Object> paramMap = Maps.newHashMap();
		MapUtils.putNotNull(paramMap, "keyword", keyword);
		MapUtils.putNotNull(paramMap, "status", status);
		MapUtils.putNotNull(paramMap, "acctType", acctType);
		MapUtils.putNotNull(paramMap, "storeCode", storeCode);

		paramMap.put(Constants.PAGE_OFFSET, offset);
		paramMap.put(Constants.PAGE_SIZE, limit);
		paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
		BootstrapPage<User> userPage = service.searchScrollPage(paramMap);
		buildDetail(userPage.getRows());
		return StatusDto.buildDataSuccessStatusDto(userPage);
	}

	//构建供应商名称
	private void buildDetail(List<User> userList) {
		if (CollectionUtils.isNotEmpty(userList)) {

			List<Long> supplierIdList = Lists.newArrayList();
			List<Long> regionIdList = Lists.newArrayList();

			for (User user : userList) {
				if (AccoutTypeEnum.PROD_SUPPLIER.equals(user.getAcctType())) {
					if (!supplierIdList.contains(user.getSupplierId())) {
						supplierIdList.add(user.getSupplierId());
					}
				} else if (AccoutTypeEnum.REGION_SUPPLIER.equals(user.getAcctType())) {
					if (!regionIdList.contains(user.getSupplierId())) {
						regionIdList.add(user.getSupplierId());
					}
				}
			}

			Map<Long, Supplier> supplierIdMap = supplierService.findSupplierIdKeyMap(supplierIdList);
			Map<Long, RegionSupplier> regionIdMap = regionSupplierService.findRegionSupplierIdKeyMap(regionIdList);
			for (User user : userList) {

				if (AccoutTypeEnum.PROD_SUPPLIER.equals(user.getAcctType())) {
					if (supplierIdMap.get(user.getSupplierId()) != null) {
						user.setSupplierName(supplierIdMap.get(user.getSupplierId()).getName());
					}

				} else if (AccoutTypeEnum.REGION_SUPPLIER.equals(user.getAcctType())) {
					if (regionIdMap.get(user.getSupplierId()) != null) {
						user.setSupplierName(regionIdMap.get(user.getSupplierId()).getName());
					}
				}
			}
		}
	}

	/**
	 * 重置密码
	 */
	@RequestMapping(value = "/resetpwd/{userId}")
	public Object resetPwd(@PathVariable Long userId) {
		service.resetPassword(userId);
		return StatusDto.buildSuccessStatusDto("密码重置成功");
	}

	/**
	 * 登录用户 修改密码
	 */
	@RequestMapping(value = "/changePwd", method = RequestMethod.POST)
	public Object changePasswordOfLoginUser(@RequestParam String password, @RequestParam String confirmPassword) {

		Long loginUserId = WebUtils.getLoggedUserId();
		if (loginUserId == null) {
			return StatusDto.buildFailureStatusDto("请先登录！");
		}

		if (StringUtils.isAnyBlank(password, confirmPassword)) {
			return StatusDto.buildFailureStatusDto("密码与确认密码 不能为空");
		}

		if (!password.equals(confirmPassword)) {
			return StatusDto.buildFailureStatusDto("两次输入的密码不一致");
		}

		User usr = new User();
		usr.setId(loginUserId);
		usr.setPlainPwd(password);
		PasswordUtil.entryptPassword(usr);
		service.update(usr);
		return StatusDto.buildSuccessStatusDto("密码修改成功");
	}

	/**
	 * 启用 禁用
	 */
	@RequestMapping(value = "/{id}/switch")
	public Object switchStatus(@PathVariable Long id, @RequestParam StatusEnum status) {
		service.switchUser(id, status);
		return StatusDto.buildSuccessStatusDto();
	}

	/**
	 * 新增或修改用户
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/saveUser")
	public Object saveUser(User user) {
		if (StringUtils.isAnyBlank(user.getUsername(), user.getName())) {
			return StatusDto.buildFailureStatusDto("用户名和名字都不能为空");
		}

		User checkUser = this.service.getUserByLoginName(user.getUsername());
		Long userId = user.getId();
		if (null != userId) {
			if (checkUser != null && !checkUser.getId().equals(userId)) {
				return StatusDto.buildFailureStatusDto("用户名" + user.getUsername() + "已存在");
			}
		} else {
			if (checkUser != null) {
				return StatusDto.buildFailureStatusDto("用户名" + user.getUsername() + "已存在");
			}
			user.setPlainPwd(User.INITIAL_PASSWORD);
			PasswordUtil.entryptPassword(user);
		}
		service.saveOrUpdateUser(user);
		return StatusDto.buildSuccessStatusDto("用户保存成功");
	}

	/**
	 * 设置用户角色
	 */
	@RequestMapping(value = "/setRole")
	public Object setUserRole(@RequestParam Long userId, @RequestParam(required = false) List<Long> roles) {
		roleService.insertUserRoles(userId, roles);
		return StatusDto.buildSuccessStatusDto("设置用户角色成功");
	}

	/**
	 * 查找用户角色
	 */
	@RequestMapping(value = "/role/{userId}")
	public Object getUserRolse(@PathVariable Long userId) {
		return roleService.findAllRoleWithCheckedByUserId(userId);
	}

	/**
	 * 同步用户信息
	 */
	@RequestMapping(value = "initUserAccount")
	public Object initUserAccount(){
		if( this.service.initUserAccount() ){
			return StatusDto.buildSuccessStatusDto("同步用户账号成功！");
		}else{
			return StatusDto.buildFailureStatusDto("同步用户账号失败！");
		}
	}
}