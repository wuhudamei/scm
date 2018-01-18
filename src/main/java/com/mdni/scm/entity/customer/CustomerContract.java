package com.mdni.scm.entity.customer;

import com.mdni.scm.common.IdEntity;

public class CustomerContract extends IdEntity {

	/**合同编号*/
	private String contractCode;
	/**客户*/
	private Customer customer;
	/**客户装修地址*/
	private String houseAddr;
	/**设计师*/
	private String designer;
	/**设计师电话**/
	private String designerMobile;
	/**监工*/
	private String supervisor;
	/**监工电话**/
	private String supervisorMobile;
	/**项目经理*/
	private String projectManager;
	/**项目经理电话*/
	private String pmMobile;
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getHouseAddr() {
		return houseAddr;
	}
	public void setHouseAddr(String houseAddr) {
		this.houseAddr = houseAddr;
	}
	public String getDesigner() {
		return designer;
	}
	public void setDesigner(String designer) {
		this.designer = designer;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public String getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	public String getPmMobile() {
		return pmMobile;
	}
	public void setPmMobile(String pmMobile) {
		this.pmMobile = pmMobile;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getDesignerMobile() {
		return designerMobile;
	}

	public void setDesignerMobile(String designerMobile) {
		this.designerMobile = designerMobile;
	}

	public String getSupervisorMobile() {
		return supervisorMobile;
	}

	public void setSupervisorMobile(String supervisorMobile) {
		this.supervisorMobile = supervisorMobile;
	}
}
