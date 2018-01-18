package com.mdni.scm.entity.order;

import java.util.Date;
import java.util.List;

import com.mdni.scm.entity.eum.OrderAcceptStatusEnum;
import com.mdni.scm.entity.eum.PlaceEnum;
import com.mdni.scm.entity.prod.Brand;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.entity.account.User;
import com.mdni.scm.entity.eum.OrderStatusEnum;

/**
 * <dl>
 * <dd>描述:订货单</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月10日 下午4:41:39</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
public class IndentOrder extends IdEntity {

	private static final long serialVersionUID = -3715479970927783499L;

	//订货单号,系统生成
	private String code;

	//合同单号
	private String contractCode;

	//订货单状态
	private OrderStatusEnum status;

	// 订货单说明
	private String note;

	//作废时,选择的作废原因
	private String reason;

	//创建人/制单人
	private User creator;

	//下单状态
	private PlaceEnum placeEnum;

	// 创建时间
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date createTime;

	@Transient
	private List<OrderItem> orderItemList;

	private String branchNo;

	private String customerName;
	/**
	 * 客户电话
	 */
	private String customerPhone;

	private String brandName;

	private String houseAddr;

	private String designer;

	private String designerMobile;

	private String supervisor;

	private String supervisorMobile;

	private String projectManager;

	private String pmMobile;

	private Long contractId;

	private OrderItem orderItem;

	private String rejectType;//驳回类型

	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date noticeInstallTime;
	/**
	 * 下载次数
	 */
	private Long downloadNumber;

	/**
	 * 最后一次下载时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date downloadDate;
	/**
	 * 备货单接收状态
	 */
	private OrderAcceptStatusEnum acceptStatus;
	/**
	 * 备货单接收时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date acceptDate;
	/**
	 * 对账时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date reconciliationTime;
	/**
	 * 对账备注
	 */
	private String  reconciliationRemarks;

	/**
	 * 实际安装时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date actualInstallationTime;

	public Date getReconciliationTime() {
		return reconciliationTime;
	}

	public void setReconciliationTime(Date reconciliationTime) {
		this.reconciliationTime = reconciliationTime;
	}

	public String getReconciliationRemarks() {
		return reconciliationRemarks;
	}

	public void setReconciliationRemarks(String reconciliationRemarks) {
		this.reconciliationRemarks = reconciliationRemarks;
	}

	public Date getActualInstallationTime() {
		return actualInstallationTime;
	}

	public void setActualInstallationTime(Date actualInstallationTime) {
		this.actualInstallationTime = actualInstallationTime;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public Long getDownloadNumber() {
		return downloadNumber;
	}

	public void setDownloadNumber(Long downloadNumber) {
		this.downloadNumber = downloadNumber;
	}

	public Date getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}

	public OrderAcceptStatusEnum getAcceptStatus() {
		return acceptStatus;
	}

	public void setAcceptStatus(OrderAcceptStatusEnum acceptStatus) {
		this.acceptStatus = acceptStatus;
	}

	public Date getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}

	public String getRejectType() {
		return rejectType;
	}

	public void setRejectType(String rejectType) {
		this.rejectType = rejectType;
	}

	public IndentOrder() {
	}

	public IndentOrder(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public OrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public PlaceEnum getPlaceEnum() {
		return placeEnum;
	}

	public void setPlaceEnum(PlaceEnum placeEnum) {
		this.placeEnum = placeEnum;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getHouseAddr() {
		return houseAddr;
	}

	public void setHouseAddr(String houseAddr) {
		this.houseAddr = houseAddr;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public Date getNoticeInstallTime() {
		return noticeInstallTime;
	}

	public void setNoticeInstallTime(Date noticeInstallTime) {
		this.noticeInstallTime = noticeInstallTime;
	}
}