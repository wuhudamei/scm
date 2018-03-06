package cn.damei.scm.rest.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.damei.scm.entity.account.Permission;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.entity.account.Role;
import cn.damei.scm.service.account.RoleService;
import cn.damei.scm.shiro.ShiroAbstractRealm;

@RestController
@RequestMapping(value = "/api/role")
public class RoleRestController extends BaseComController<RoleService, Role> {

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