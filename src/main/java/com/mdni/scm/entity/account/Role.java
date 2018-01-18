package com.mdni.scm.entity.account;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.mdni.scm.common.IdEntity;

/**
 * 
 * <dl>
 * <dd>描述: 角色</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月8日 下午4:29:40</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
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