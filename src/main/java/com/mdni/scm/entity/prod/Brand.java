package com.mdni.scm.entity.prod;

import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.eum.StatusEnum;

/**
 * 
 * <dl>
 * <dd>描述: 品牌</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017-6-23 14:33:21</dd>
 * <dd>创建人： 张俊奎</dd>
 * </dl>
 */
public class Brand extends IdEntity {

	private static final long serialVersionUID = 1391361468559826039L;

	// 编码 
	private String code;

	//名称
	private String brandName;

	// 品牌拼音首字母
	private String pinyinInitial;

	//logo
	private String logo;

	//描述
	private String description;

	//启用状态：停用、启用
	private StatusEnum status;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getPinyinInitial() {
		return pinyinInitial;
	}

	public void setPinyinInitial(String pinyinInitial) {
		this.pinyinInitial = pinyinInitial;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}