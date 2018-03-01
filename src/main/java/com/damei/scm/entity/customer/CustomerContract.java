package com.damei.scm.entity.customer;

import com.damei.scm.common.IdEntity;

public class CustomerContract extends IdEntity {

	private String contractCode;
	private Customer customer;
	private String houseAddr;
	private String designer;
	private String designerMobile;
	private String supervisor;
	private String supervisorMobile;
	private String projectManager;
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
