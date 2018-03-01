package com.damei.scm.entity.account;

import com.damei.scm.common.IdEntity;
import org.springframework.data.annotation.Transient;

public class Permission extends IdEntity {

	private static final long serialVersionUID = -5239923863483634290L;

	private String name;

	private String module;

	private Integer seq;

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