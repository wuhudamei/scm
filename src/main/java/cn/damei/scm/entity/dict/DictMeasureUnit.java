package cn.damei.scm.entity.dict;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.entity.eum.StatusEnum;

public class DictMeasureUnit extends IdEntity {
	private static final long serialVersionUID = -2938165343415851251L;

	private String name;

	private StatusEnum status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

}