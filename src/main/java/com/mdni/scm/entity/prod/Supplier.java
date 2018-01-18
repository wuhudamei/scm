package com.mdni.scm.entity.prod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.eum.StatusEnum;

/**
 * 
 * <dl>
 * <dd>描述：商品供货商</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月12日 下午2:29:59</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier extends IdEntity {

	private static final long serialVersionUID = 5550923062492753778L;

	//供应商编码
	private String code;

	// 名称
	private String name;

	//中文拼音首字母
	private String pinyinInitial;

	// 联系人
	private String contactor;
	//联系人手机
	private String mobile;

	// 公司固话 
	private String phone;

	//公司地址
	private String address;

	//供应商描述
	private String description;

	//所属 区域供应商
	private RegionSupplier regionSupplier;

	// 启用状态：停用、启用
	private StatusEnum status;
	private String supplierAbbreviation;//供应商简称
	private String cooperativeBrandName;//合作品牌名称
	private String manager;//负责人
	private String managerMobile;//负责人电话
	private String businessManager;//业务负责人
	private String businessManagerMobile;//业务负责人电话
	private String openingBank;//开户行
	private String accountNumber;//账号
	private String taxRegistrationCertificateImageUrl;//税务登记证图片
	private String taxRegistrationCertificateImageFullUrl;//税务登记证图片完整路径
	private String businessLicenseImageUrl;//营业执照图片
	private String businessLicenseImageFullUrl;//营业执照图片完整路径
	private String taxpayerIdentificationNumber;//纳税人识别编号

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