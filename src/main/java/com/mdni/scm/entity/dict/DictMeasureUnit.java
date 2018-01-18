package com.mdni.scm.entity.dict;

import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.eum.StatusEnum;

/**
 * <dl>
 * <dd>描述: 计量单位</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 下午2:56:28</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
public class DictMeasureUnit extends IdEntity {
	private static final long serialVersionUID = -2938165343415851251L;
	//等级名称	
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