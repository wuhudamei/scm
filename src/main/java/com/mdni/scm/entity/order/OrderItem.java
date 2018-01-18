package com.mdni.scm.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.entity.eum.PayStatusEnum;
import com.mdni.scm.entity.eum.SendStatusEnum;
import com.mdni.scm.entity.prod.Brand;
import com.mdni.scm.entity.prod.Catalog;
import com.mdni.scm.entity.prod.Sku;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <dl>
 * <dd>描述: 订货单详细</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月10日 下午5:09:33</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
public class OrderItem extends IdEntity {

	private static final long serialVersionUID = 4915910286021857508L;

	//订货单Id
	private Long orderId;

	//供货商Id
	private Long supplierId;

	//订货的商品
	private Sku sku;

	//订货单的供货价
	private BigDecimal supplyPrice;

	//订货数量
	private BigDecimal quantity;

	private String orderCode;//订单号

	private String contractCode;//项目号

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

	//预计安装时间
	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date installDate;

	private SendStatusEnum status;

	private PayStatusEnum payStatus;

	//复尺结果
	private String reviewSizeResult;

	//其他费用金额
	private BigDecimal otherFee;

	//其他费用 收费说明
	private String otherFeeNote;

	//对应其他费用集合
	private List<OrderItemOtherFee> otherFeesList;

	//是否有其它费用
	private Boolean hasOtherFee;

	//发货备注
	private String note;

	//安装位置
	private String installationLocation;

	//品牌
	private Brand brand;

	private Long brandId;

	private Catalog catalog;
	//通知安装时间
	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date noticeInstallDate;
	//入库时间
	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date storageDate;
	//实际安装时间
	@JsonFormat(pattern = DateUtil.DATE_PATTERN)
	private Date actualInstallDate;
	/**片数**/
	private Integer tabletNum;
	/****/
	private String specUnit;
	/**
	 * 单位数量
	 */
	private String unitQuantity;


	/**
	 *
	 * 以下字段 为导出 需要
	 *
	 */
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