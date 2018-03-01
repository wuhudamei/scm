package com.damei.scm.entity.account;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.damei.scm.common.IdEntity;

public class Role extends IdEntity {

	private static final long serialVersionUID = 924053401213223985L;

	private String name;

	private String description;

	@Transient
	private Boolean checked;
	@Transient
	private List<Permission> permissionList;

	public Role() {
	}

	public Role(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
}