package com.mdni.scm.entity.account;

import com.mdni.scm.common.IdEntity;

/**
 * 
 * <dl>
 * <dd>描述:角色-权限 关联关系</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月8日 下午4:32:42</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
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