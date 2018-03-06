package cn.damei.scm.entity.account;

import cn.damei.scm.common.IdEntity;

public class RolePermission extends IdEntity {
	private static final long serialVersionUID = -6745089783402793598L;

	public RolePermission() {
		super();
	}

	public RolePermission(Long roleId, Long permissionId) {
		super();
		this.roleId = roleId;
		this.permissionId = permissionId;
	}

	private Long roleId;

	private Long permissionId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
}