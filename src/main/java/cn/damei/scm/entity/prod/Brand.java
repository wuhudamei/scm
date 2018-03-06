package cn.damei.scm.entity.prod;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.entity.eum.StatusEnum;

public class Brand extends IdEntity {

	private static final long serialVersionUID = 1391361468559826039L;


	private String code;


	private String brandName;


	private String pinyinInitial;


	private String logo;


	private String description;

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