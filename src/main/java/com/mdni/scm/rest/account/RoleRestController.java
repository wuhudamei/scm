package com.mdni.scm.rest.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.entity.account.Permission;
import com.mdni.scm.entity.account.Role;
import com.mdni.scm.service.account.RoleService;
import com.mdni.scm.shiro.ShiroAbstractRealm;

/**
 * 
 * <dl>
 * <dd>描述: 角色</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月9日 下午3:59:42</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/role")
public class RoleRestController extends BaseComController<RoleService, Role> {

	/**
	 * 保存更新角色
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public Object saveOrUpdate(Role role) {

		if (StringUtils.isBlank(role.getName())) {
			return StatusDto.buildFailureStatusDto("角色名称不能为空！");
		}

		if (this.service.isRoleNameExist(role)) {
			return StatusDto.buildFailureStatusDto("已存在该角色名!");
		}
		service.saveOrUpdate(role);
		return StatusDto.buildSuccessStatusDto("角色保存成功");
	}

	/**
	 * 查询角色绑定的权限及未绑定权限
	 */
	@RequestMapping(value = "/findRolePermission/{roleId}", method = RequestMethod.GET)
	public Object findRolePermission(@PathVariable Long roleId) {

		Map<String, Object> modulePermListMap = new LinkedHashMap<>();
		List<Permission> allPermission = service.findAllPermissionWithCheckedByRoleId(roleId);
		for (Permission perm : allPermission) {
			String module = perm.getModule();
			@SuppressWarnings("unchecked")
			List<Permission> permList = (List<Permission>) modulePermListMap.get(module);
			if (permList == null) {
				permList = new ArrayList<Permission>();
				modulePermListMap.put(module, permList);
			}
			permList.add(perm);
		}
		return StatusDto.buildDataSuccessStatusDto(modulePermListMap);
	}

	/**
	 * 角色设置权限
	 */
	@RequestMapping(value = "/setPerm", method = RequestMethod.POST)
	public Object setRolePerm(@RequestParam Long roleId, @RequestParam(required = false) List<Long> permissions) {
		service.insertRolePermission(roleId, permissions);

		RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		Collection<Realm> reamCollections = securityManager.getRealms();
		for (Realm real : reamCollections) {
			if (real instanceof ShiroAbstractRealm) {
				((ShiroAbstractRealm) real).clearAuthorization();
			}
		}
		return StatusDto.buildSuccessStatusDto("权限设置成功");
	}
}