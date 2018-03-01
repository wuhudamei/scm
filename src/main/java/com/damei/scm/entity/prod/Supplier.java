package com.damei.scm.entity.prod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.damei.scm.common.IdEntity;
import com.damei.scm.entity.eum.StatusEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier extends IdEntity {

	private static final long serialVersionUID = 5550923062492753778L;


	private String code;


	private String name;


	private String pinyinInitial;


	private String contactor;

	private String mobile;


	private String phone;


	private String address;


	private String description;


	private RegionSupplier regionSupplier;


	private StatusEnum status;
	private String supplierAbbreviation;
	private String cooperativeBrandName;
	private String manager;
	private String managerMobile;
	private String businessManager;
	private String businessManagerMobile;
	private String openingBank;
	private String accountNumber;
	private String taxRegistrationCertificateImageUrl;
	private String taxRegistrationCertificateImageFullUrl;
	private String businessLicenseImageUrl;
	private String businessLicenseImageFullUrl;
	private String taxpayerIdentificationNumber;

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

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public String getPinyinInitial() {
		return pinyinInitial;
	}

	public void setPinyinInitial(String pinyinInitial) {
		this.pinyinInitial = pinyinInitial;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RegionSupplier getRegionSupplier() {
		return regionSupplier;
	}

	public void setRegionSupplier(RegionSupplier regionSupplier) {
		this.regionSupplier = regionSupplier;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSupplierAbbreviation() {
		return supplierAbbreviation;
	}

	public void setSupplierAbbreviation(String supplierAbbreviation) {
		this.supplierAbbreviation = supplierAbbreviation;
	}

	public String getCooperativeBrandName() {
		return cooperativeBrandName;
	}

	public void setCooperativeBrandName(String cooperativeBrandName) {
		this.cooperativeBrandName = cooperativeBrandName;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManagerMobile() {
		return managerMobile;
	}

	public void setManagerMobile(String managerMobile) {
		this.managerMobile = managerMobile;
	}

	public String getBusinessManager() {
		return businessManager;
	}

	public void setBusinessManager(String businessManager) {
		this.businessManager = businessManager;
	}

	public String getBusinessManagerMobile() {
		return businessManagerMobile;
	}

	public void setBusinessManagerMobile(String businessManagerMobile) {
		this.businessManagerMobile = businessManagerMobile;
	}

	public String getOpeningBank() {
		return openingBank;
	}

	public void setOpeningBank(String openingBank) {
		this.openingBank = openingBank;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getTaxRegistrationCertificateImageUrl() {
		return taxRegistrationCertificateImageUrl;
	}

	public void setTaxRegistrationCertificateImageUrl(String taxRegistrationCertificateImageUrl) {
		this.taxRegistrationCertificateImageUrl = taxRegistrationCertificateImageUrl;
	}

	public String getBusinessLicenseImageUrl() {
		return businessLicenseImageUrl;
	}

	public void setBusinessLicenseImageUrl(String businessLicenseImageUrl) {
		this.businessLicenseImageUrl = businessLicenseImageUrl;
	}

	public String getTaxpayerIdentificationNumber() {
		return taxpayerIdentificationNumber;
	}

	public void setTaxpayerIdentificationNumber(String taxpayerIdentificationNumber) {
		this.taxpayerIdentificationNumber = taxpayerIdentificationNumber;
	}

	public String getTaxRegistrationCertificateImageFullUrl() {
		return taxRegistrationCertificateImageFullUrl;
	}

	public void setTaxRegistrationCertificateImageFullUrl(String taxRegistrationCertificateImageFullUrl) {
		this.taxRegistrationCertificateImageFullUrl = taxRegistrationCertificateImageFullUrl;
	}

	public String getBusinessLicenseImageFullUrl() {
		return businessLicenseImageFullUrl;
	}

	public void setBusinessLicenseImageFullUrl(String businessLicenseImageFullUrl) {
		this.businessLicenseImageFullUrl = businessLicenseImageFullUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Supplier other = (Supplier) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}