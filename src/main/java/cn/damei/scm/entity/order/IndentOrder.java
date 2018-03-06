package cn.damei.scm.entity.order;

import java.util.Date;
import java.util.List;

import cn.damei.scm.entity.eum.OrderAcceptStatusEnum;
import cn.damei.scm.entity.eum.PlaceEnum;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import cn.damei.scm.common.IdEntity;
import cn.damei.scm.common.utils.DateUtil;
import cn.damei.scm.entity.account.User;
import cn.damei.scm.entity.eum.OrderStatusEnum;

public class IndentOrder extends IdEntity {

	private static final long serialVersionUID = -3715479970927783499L;


	private String code;


	private String contractCode;


	private OrderStatusEnum status;


	private String note;


	private String reason;


	private User creator;


	private PlaceEnum placeEnum;


	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date createTime;

	@Transient
	private List<OrderItem> orderItemList;

	private String branchNo;

	private String customerName;

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

	private String rejectType;

	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date noticeInstallTime;

	private Long downloadNumber;


	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date downloadDate;

	private OrderAcceptStatusEnum acceptStatus;

	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date acceptDate;

	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date reconciliationTime;

	private String  reconciliationRemarks;

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