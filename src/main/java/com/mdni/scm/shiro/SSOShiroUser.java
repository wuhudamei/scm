package com.mdni.scm.shiro;

import com.mdni.scm.entity.eum.AccoutTypeEnum;

import java.util.List;

public class SSOShiroUser extends ShiroUser {

	private static final long serialVersionUID = 2899514607512650230L;

	private List<String> roleNameList;

	private List<String> permissionList;

	public SSOShiroUser(Long id, String username, String name, AccoutTypeEnum acctType,
						Long supplierId,String storeCode) {
		super(id, username, name, acctType,supplierId,storeCode);
	}

	public List<String> getRoleNameList() {
		return roleNameList;
	}

	public void setRoleNameList(List<String> roleNameList) {
		this.roleNameList = roleNameList;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}
}
