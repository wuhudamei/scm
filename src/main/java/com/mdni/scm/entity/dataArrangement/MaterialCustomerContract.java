package com.mdni.scm.entity.dataArrangement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mdni.scm.common.IdEntity;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.common.utils.ExcelTitle;
import com.mdni.scm.entity.eum.dataArrangement.MetarialContractStatusEnum;

import java.math.BigDecimal;
import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialCustomerContract extends IdEntity{

    //预算号
    @ExcelTitle(title = "预算号", order = 1)
    private String budgetNo;

    //项目编号
    @ExcelTitle(title = "项目编号", order = 2)
    private String projectCode;

    //客户姓名
    @ExcelTitle(title = "客户姓名", order = 4)
    private String customerName;

    //客户手机号
    @ExcelTitle(title = "客户电话", order = 5)
    private String customerPhone;

    //项目地址
    @ExcelTitle(title = "施工地址", order = 7)
    private String projectAddress;

    //建筑面积
    @ExcelTitle(title = "建筑面积", order = 8)
    private Double budgetArea;

    //房型
    @ExcelTitle(title = "房型", order = 6)
    private String houseLayout;

    //设计师姓名
    @ExcelTitle(title = "设计师", order = 15)
    private String designerName;

    //设计师手机号
    @ExcelTitle(title = "设计师手机号码", order = 16)
    private String designerPhone;

    //客服
    @ExcelTitle(title = "客服", order = 14)
    private String customerService;

    //客服手机号
    private String customerServicePhone;

    //项目经理姓名
    private String managerName;

    //项目经理手机号
    private String managerPhone;

    //质检员
    private String inspectorName;

    //质检员手机号
    private String inspectorPhone;

    //合同签订时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date contractSignDate;
    //导入 合同录入时间 曲线救国
    @ExcelTitle(title = "签订日期", order = 3)
    private Date  exportContractSignDate;

    //计划开始时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date planStartDate;

    //计划结束时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date planFinishDate;

    //工程造价
    @ExcelTitle(title = "合同金额", order = 12)
    private Double engineeringCost;

    //旧房拆改费
    @ExcelTitle(title = "旧房拆改", order = 11)
    private Double dismantleFee;

    //变更费
    @ExcelTitle(title = "变更合计", order = 13)
    private Double changeFee;

    //是否有电梯
    private Integer haveElevator;
    // 是否有电梯 导入用 曲线救国
    @ExcelTitle(title = "是否有电梯", order = 17)
    private String exportHaveElevator;
    //备注
    private String remarks;

    //创建时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date createTime;

    //创建人
    private String createAccount;

    //合同状态
    private MetarialContractStatusEnum contractStatus;
    @ExcelTitle(title = "所选套餐", order = 9)
    private String  meal;
    private BigDecimal budgetFee;
    //预算的导入  曲线救国
    @ExcelTitle(title = "预算报价", order = 10)
    private String exportBudgetFee;
    //录入人
    private String keyboarder;
    //录入时间
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date inputTime;
    //审核人
    private String verifier;
    //审核时间
    private Date reviewTime;

    /**
     * 竣工时间
     */
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private Date completeDate;

    public String getExportBudgetFee() {
        return exportBudgetFee;
    }

    public void setExportBudgetFee(String exportBudgetFee) {
        this.exportBudgetFee = exportBudgetFee;
    }

    public String getBudgetNo() {
        return budgetNo;
    }

    public void setBudgetNo(String budgetNo) {
        this.budgetNo = budgetNo == null ? null : budgetNo.trim();
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode == null ? null : projectCode.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone == null ? null : customerPhone.trim();
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress == null ? null : projectAddress.trim();
    }

    public Double getBudgetArea() {
        return budgetArea;
    }

    public void setBudgetArea(Double budgetArea) {
        this.budgetArea = budgetArea;
    }

    public String getHouseLayout() {
        return houseLayout;
    }

    public void setHouseLayout(String houseLayout) {
        this.houseLayout = houseLayout == null ? null : houseLayout.trim();
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName == null ? null : designerName.trim();
    }

    public String getDesignerPhone() {
        return designerPhone;
    }

    public void setDesignerPhone(String designerPhone) {
        this.designerPhone = designerPhone == null ? null : designerPhone.trim();
    }

    public String getCustomerService() {
        return customerService;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService == null ? null : customerService.trim();
    }

    public String getCustomerServicePhone() {
        return customerServicePhone;
    }

    public void setCustomerServicePhone(String customerServicePhone) {
        this.customerServicePhone = customerServicePhone == null ? null : customerServicePhone.trim();
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName == null ? null : managerName.trim();
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone == null ? null : managerPhone.trim();
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName == null ? null : inspectorName.trim();
    }

    public String getInspectorPhone() {
        return inspectorPhone;
    }

    public void setInspectorPhone(String inspectorPhone) {
        this.inspectorPhone = inspectorPhone == null ? null : inspectorPhone.trim();
    }

    public Date getContractSignDate() {
        return contractSignDate;
    }

    public void setContractSignDate(Date contractSignDate) {
        this.contractSignDate = contractSignDate;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanFinishDate() {
        return planFinishDate;
    }

    public void setPlanFinishDate(Date planFinishDate) {
        this.planFinishDate = planFinishDate;
    }

    public Double getEngineeringCost() {
        return engineeringCost;
    }

    public void setEngineeringCost(Double engineeringCost) {
        this.engineeringCost = engineeringCost;
    }

    public Double getDismantleFee() {
        return dismantleFee;
    }

    public void setDismantleFee(Double dismantleFee) {
        this.dismantleFee = dismantleFee;
    }

    public Double getChangeFee() {
        return changeFee;
    }

    public void setChangeFee(Double changeFee) {
        this.changeFee = changeFee;
    }

    public Integer getHaveElevator() {
        return haveElevator;
    }

    public void setHaveElevator(Integer haveElevator) {
        this.haveElevator = haveElevator;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount == null ? null : createAccount.trim();
    }

    public MetarialContractStatusEnum getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(MetarialContractStatusEnum contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public BigDecimal getBudgetFee() {
        return budgetFee;
    }

    public void setBudgetFee(BigDecimal budgetFee) {
        this.budgetFee = budgetFee;
    }

    public String getExportHaveElevator() {
        return exportHaveElevator;
    }

    public void setExportHaveElevator(String exportHaveElevator) {
        this.exportHaveElevator = exportHaveElevator;
    }

    public Date getExportContractSignDate() {
        return exportContractSignDate;
    }

    public void setExportContractSignDate(Date exportContractSignDate) {
        this.exportContractSignDate = exportContractSignDate;
    }

    public String getKeyboarder() {
        return keyboarder;
    }

    public void setKeyboarder(String keyboarder) {
        this.keyboarder = keyboarder;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }
}