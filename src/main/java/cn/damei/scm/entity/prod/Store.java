package cn.damei.scm.entity.prod;

import cn.damei.scm.common.IdEntity;

public class Store extends IdEntity {


	private String name;

	private String code;
	

	public Store(String code) {
		this.code = code;
	}

	public Store(){
		
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
