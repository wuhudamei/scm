package com.mdni.scm.entity.account;

import com.mdni.scm.common.IdEntity;
import org.springframework.data.annotation.Transient;

/**
 * <dl>
 * <dd>描述:权限</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月8日 下午4:27:59</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
public class Permission extends IdEntity {

	private static final long serialVersionUID = -5239923863483634290L;

	// 权限名称 
	private String name;

	// 模块
	private String module;

	private Integer seq;

	// 权限值 
	private String permission;

	@Transient
	private Boolean checked;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module == null ? null : module.trim();
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission == null ? null : permission.trim();
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}