package com.mdni.scm.entity.prod;

import com.mdni.scm.common.IdEntity;
/**
 * 
 * 大诚若谷信息技术有限公司
 * 功能：门店实体
 * 作者:张俊奎
 * 时间：2017年7月5日上午10:58:33
 */
public class Store extends IdEntity{

	/**门店名称*/
	private String name;
	/**门店编码*/
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
