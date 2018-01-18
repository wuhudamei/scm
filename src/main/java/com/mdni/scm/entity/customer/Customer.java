package com.mdni.scm.entity.customer;

import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.prod.Store;

public class Customer extends IdEntity{
	
	/**客户代码*/
	private String code;
	/**客户名字*/
	private String name;
	/**客户手机号*/
	private String mobile;
	/**所属门店*/
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
