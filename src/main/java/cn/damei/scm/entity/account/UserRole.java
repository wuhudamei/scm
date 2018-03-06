package cn.damei.scm.entity.account;

import cn.damei.scm.common.IdEntity;

public class UserRole extends IdEntity {

	private static final long serialVersionUID = -7573825350524674136L;

	private Long userId;

	private Long roleId;

	public UserRole() {
		super();
	}

	public UserRole(Long userId, Long roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}