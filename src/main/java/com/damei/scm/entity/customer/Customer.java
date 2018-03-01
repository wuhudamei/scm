package com.damei.scm.entity.customer;

import com.damei.scm.common.IdEntity;
import com.damei.scm.entity.prod.Store;

public class Customer extends IdEntity{
	
	private String code;
	private String name;
	private String mobile;
	private Store store;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	

	
}
