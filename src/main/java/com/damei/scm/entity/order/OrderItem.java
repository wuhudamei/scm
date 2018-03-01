package com.damei.scm.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.damei.scm.common.IdEntity;
import com.damei.scm.common.utils.DateUtil;
import com.damei.scm.entity.eum.PayStatusEnum;
import com.damei.scm.entity.eum.SendStatusEnum;
import com.damei.scm.entity.prod.Brand;
import com.damei.scm.entity.prod.Catalog;
import com.damei.scm.entity.prod.Sku;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderItem extends IdEntity {

	private static final long serialVersionUID = 4915910286021857508L;


	private Long orderId;


	private Long supplierId;


	private Sku sku;


	private BigDecimal supplyPrice;


	private BigDecimal quantity;

	private String orderCode;

	private String contractCode;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date installDate;

	private SendStatusEnum status;

	private PayStatusEnum payStatus;


	private String reviewSizeResult;


	private BigDecimal otherFee;


	private String otherFeeNote;


	private List<OrderItemOtherFee> otherFeesList;


	private Boolean hasOtherFee;


	private String note;


	private String installationLocation;


	private Brand brand;

	private Long brandId;

	private Catalog catalog;

	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date noticeInstallDate;

	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date storageDate;

	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date actualInstallDate;

	private Integer tabletNum;

	private String specUnit;

	private String unitQuantity;

	private String orderStatus;
	private String orderPayStatus;
	private String name;
	private String mobile;
	private String houseAddr;
	private String designer;
	private String designerMobile;
	private String supervisor;
	private String supervisorMobile;
	private String projectManager;
	private String pmMobile;

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderPayStatus() {
		return orderPayStatus;
	}

	public void setOrderPayStatus(String orderPayStatus) {
		this.orderPayStatus = orderPayStatus;
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

	public String getDesignerMobile() {
		return designerMobile;
	}

	public void setDesignerMobile(String designerMobile) {
		this.designerMobile = designerMobile;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getSupervisorMobile() {
		return supervisorMobile;
	}

	public void setSupervisorMobile(String supervisorMobile) {
		this.supervisorMobile = supervisorMobile;
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

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Date getInstallDate() {
		return installDate;
	}

	public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}

	public SendStatusEnum getStatus() {
		return status;
	}

	public void setStatus(SendStatusEnum status) {
		this.status = status;
	}

	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getHasOtherFee() {
		return hasOtherFee;
	}

	public String getReviewSizeResult() {
		return reviewSizeResult;
	}

	public void setReviewSizeResult(String reviewSizeResult) {
		this.reviewSizeResult = reviewSizeResult;
	}

	public BigDecimal getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(BigDecimal supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	public BigDecimal getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(BigDecimal otherFee) {
		this.otherFee = otherFee;
	}

	public String getOtherFeeNote() {
		return otherFeeNote;
	}

	public void setOtherFeeNote(String otherFeeNote) {
		this.otherFeeNote = otherFeeNote;
	}

	public List<OrderItemOtherFee> getOtherFeesList() {
		return otherFeesList;
	}

	public void setOtherFeesList(List<OrderItemOtherFee> otherFeesList) {
		this.otherFeesList = otherFeesList;
	}

	public void setHasOtherFee(Boolean hasOtherFee) {
		this.hasOtherFee = hasOtherFee;
	}
	public String getInstallationLocation() {
		return installationLocation;
	}

	public void setInstallationLocation(String installationLocation) {
		this.installationLocation = installationLocation;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Date getNoticeInstallDate() {
		return noticeInstallDate;
	}

	public void setNoticeInstallDate(Date noticeInstallDate) {
		this.noticeInstallDate = noticeInstallDate;
	}

	public Date getActualInstallDate() {
		return actualInstallDate;
	}

	public void setActualInstallDate(Date actualInstallDate) {
		this.actualInstallDate = actualInstallDate;
	}

	public Date getStorageDate() {
		return storageDate;
	}

	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}

	public Integer getTabletNum() {
		return tabletNum;
	}

	public void setTabletNum(Integer tabletNum) {
		this.tabletNum = tabletNum;
	}

	public String getSpecUnit() {
		return specUnit;
	}

	public void setSpecUnit(String specUnit) {
		this.specUnit = specUnit;
	}

	public String getUnitQuantity() {
		return unitQuantity;
	}

	public void setUnitQuantity(String unitQuantity) {
		this.unitQuantity = unitQuantity;
	}
}