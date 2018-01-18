package com.mdni.scm.entity.account;

import com.mdni.scm.common.IdEntity;

/**
 * 
 * <dl>
 * <dd>描述: 用户-角色 关联关系</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月8日 下午5:11:13</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
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