package com.mdni.scm.rest.dataArrangement;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.utils.*;
import com.mdni.scm.entity.dataArrangement.MaterialCustomerContract;
import com.mdni.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import com.mdni.scm.entity.eum.dataArrangement.MetarialContractStatusEnum;
import com.mdni.scm.service.dataArrangement.MaterialCustomerContractService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * <dl>
 * <dd>描述:客户合同Controller</dd>
 * <dd>创建人： Chaos</dd>
 * </dl>
 */
@RestController
@RequestMapping("/api/customerContract")
public class MaterialCustomerContractController extends BaseComController<MaterialCustomerContractService, MaterialCustomerContract> {
	public static final String Y = "是";
	public static final String N = "否";
	@Autowired
	private MaterialCustomerContractService materialCustomerContractService;

	/**
	 * 查询所有的合同信息
	 * @param customerName 客户姓名
	 * @param customerPhone 客户手机号
	 * @param contractStatus 合同状态
	 * @param budgetNo 		合同预算号
	 */
	@RequestMapping("/list")
	public Object findAll(@RequestParam(required = false) String budgetNo,
						  @RequestParam(required = false) String customerName,
						  @RequestParam(required = false) String customerPhone,
						  @RequestParam(required = false) String contractStatus,
						  @RequestParam(defaultValue = "0") int offset,
						  @RequestParam(defaultValue = "20") int limit){
		Map<String, Object> params = new HashMap<String, Object>();
		MapUtils.putNotNull(params, "customerName", customerName);
		MapUtils.putNotNull(params, "customerPhone", customerPhone);
		MapUtils.putNotNull(params, "contractStatus", contractStatus);
		MapUtils.putNotNull(params, "budgetNo", budgetNo);

		if( StringUtils.isNotBlank(customerName ) || StringUtils.isNotBlank(customerPhone ) || StringUtils.isNotBlank(contractStatus )|| StringUtils.isNotBlank(budgetNo )  ){
			params.put(Constants.PAGE_OFFSET, offset);
			params.put(Constants.PAGE_SIZE, limit);
			return StatusDto.buildDataSuccessStatusDto(this.materialCustomerContractService.searchScrollPage(params));
		}else{
			return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
		}
	}

	/**
	 * 插入/修改合同信息
	 * @param materialCustomerContractConfirm 合同信息
	 */
	@RequestMapping(value = "/saveConfirmData", method = RequestMethod.POST)
	public Object saveConfirmData(@RequestBody MaterialCustomerContractConfirm materialCustomerContractConfirm){
		this.materialCustomerContractService.saveConfirmData(materialCustomerContractConfirm);
		return StatusDto.buildDataSuccessStatusDto("操作成功！");
	}

	/**
	 * 修改合同状态
	 * @param id 合同id
	 * @param contractStatus 合同状态
	 */
	@RequestMapping("/updateStatus")
	public Object updateStatus(@RequestParam Long id,@RequestParam MetarialContractStatusEnum contractStatus){
		this.materialCustomerContractService.updateStatus(id,contractStatus);
		return StatusDto.buildSuccessStatusDto("操作成功！！！");
	}

	@RequestMapping(value ="getContractDetail", method = RequestMethod.GET)
	public Object getContractDetail(@RequestParam Long id){
		return StatusDto.buildDataSuccessStatusDto(this.materialCustomerContractService.getContractDetail(id));
	}

	@RequestMapping("/getById")
	public Object getById(@RequestParam Long id){
		return StatusDto.buildDataSuccessStatusDto(this.materialCustomerContractService.getById(id));
	}

	@RequestMapping("/update")
	public Object update(MaterialCustomerContract customerContract){
		this.materialCustomerContractService.update(customerContract);
		return StatusDto.buildSuccessStatusDto();
	}
	/**
	 * 客户合同导入模板
	 * @author Ryze
	 * @date 2017-8-7 10:52:13
	 * @param
	 */
	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
	public void downloadProductTemplate(HttpServletResponse resp) {
		final String tempFileName = "customer_contract.xlsx";
		ServletOutputStream out = null;
		resp.setContentType("application/x-msdownload");
		resp.addHeader("Content-Disposition", "attachment; filename='" + tempFileName + "'");
		Resource resource = new DefaultResourceLoader().getResource(tempFileName);
		File randomCopyFile = null;
		Workbook wb = null;
		try {
			out = resp.getOutputStream();
			File file = resource.getFile();
			String randomCopyFileName = UUID.randomUUID().toString() + ".xlsx";
			randomCopyFile = new File(file.getParent(), randomCopyFileName);
			FileUtils.copyFile(file, randomCopyFile);
			wb = WorkbookFactory.create(randomCopyFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				wb.write(out);
				wb.close();
			} catch (IOException e) {
			}
			if (randomCopyFile != null) {
				FileUtils.deleteFile(randomCopyFile);
			}
		}
	}
	/**
	 * 客户合同导入
	 * @author Ryze
	 * @date 2017-8-7 10:53:02
	 * @param
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public Object importProduct(@RequestParam("file") MultipartFile file) {
		List<MaterialCustomerContract> productDtoList = null;
		try {
			productDtoList = ExcelUtil.getInstance().readExcel2ObjsByStream(file.getInputStream(), MaterialCustomerContract.class);
		} catch (IOException e) {
			return StatusDto.buildFailureStatusDto("数据格式有问题，请检查");
		}
		if (CollectionUtils.isEmpty(productDtoList)) {
			return StatusDto.buildFailureStatusDto("Excel文件中没有客户合同数据");
		}
		//校验
		String errorMsg = validateImportExcelProductData(productDtoList);
		if (StringUtils.isNotEmpty(errorMsg)) {
			return StatusDto.buildFailureStatusDto(errorMsg);
		}
		//执行导入操作
		executeEncapsulateProduct(productDtoList);
		return StatusDto.buildSuccessStatusDto("商品批量导入成功");
	}
	/**
	 * 校验导入的客户合同数据
	 * 返回错误消息,如果没有错误则返回 null
	 * @author Ryze
	 * @date 2017-8-7 15:36:50
	 * @param customerContractList
	 */
	private String validateImportExcelProductData(List<MaterialCustomerContract> customerContractList) {

		int rowIndex = 1;
		String errorMsg = null;

		final String rowNumKey = "rowNum";
		final String errorMsgKey = "errorMsg";
		//第rowNum行{errorMsg}
		String errorMsgTmp = "第" + rowNumKey + "行{" + errorMsgKey + "}";
		StringBuilder errorBuf = new StringBuilder();
		for (MaterialCustomerContract productDto : customerContractList) {
			if (StringUtils.isBlank(productDto.getBudgetNo())) {
				errorBuf.append("合同预算号不能为空;");
			}
			if (StringUtils.isBlank(productDto.getProjectCode())) {
				errorBuf.append("项目编号不能为空;");
			}
			if (StringUtils.isBlank(productDto.getCustomerName())) {
				errorBuf.append("客户姓名不能为空;");
			}
			if (StringUtils.isBlank(productDto.getCustomerPhone())) {
				errorBuf.append("客户电话不能为空;");
			}
			if (StringUtils.isBlank(productDto.getProjectAddress())) {
				errorBuf.append("工程地址不能为空;");
			}
			try {
				//工程预算的 转换 拦截校验;
				String exportBudgetFee = productDto.getExportBudgetFee();
				if(!StringUtils.isEmpty(exportBudgetFee)) {
					new BigDecimal(exportBudgetFee);
				}
			}catch (Exception e){
				errorBuf.append("工程预算数值有问题;");
			}
			rowIndex++;
			if (errorBuf.length() > 0) {
				//如果有错误，则break循环 返回
				break;
			}
		}
		if (errorBuf.length() > 0) {
			errorMsg = errorMsgTmp.replace(rowNumKey, String.valueOf(rowIndex)).replace(errorMsgKey,
					errorBuf.toString());
		}
		return errorMsg;
	}

	/**
	 * 执行客户合同封装 导入
	 * @author Ryze
	 * @date 2017-8-7 15:36:50
	 * @param customerContractList
	 */
	private void executeEncapsulateProduct(List<MaterialCustomerContract> customerContractList) {
		for (MaterialCustomerContract materialCustomerContract:customerContractList) {
			//设置 创建日期创建人
			materialCustomerContract.setCreateTime(new Date());
			materialCustomerContract.setCreateAccount(WebUtils.getLoggedUser().getLoginName());
			//是否有电梯的转换
			String exportHaveElevator = materialCustomerContract.getExportHaveElevator();
			if(!StringUtils.isEmpty(exportHaveElevator)){
				if(exportHaveElevator.equals(Y)){
					materialCustomerContract.setHaveElevator(1);
				}else if(exportHaveElevator.equals(N)){
					materialCustomerContract.setHaveElevator(0);
				}
			}
			//工程预算的 转换;
			String exportBudgetFee = materialCustomerContract.getExportBudgetFee();
			if(!StringUtils.isEmpty(exportBudgetFee)) {
				materialCustomerContract.setBudgetFee(new BigDecimal(exportBudgetFee));
			}
			//日期类 转换
			Date exportContractSignDate = materialCustomerContract.getExportContractSignDate();
			if(exportContractSignDate!=null) {
				materialCustomerContract.setContractSignDate(DateUtil.parseToDate(DateUtil.formatDate(exportContractSignDate)));
			}
			//添加状态   未核对
			materialCustomerContract.setContractStatus(MetarialContractStatusEnum.NOTCHECKED);
			//添加
			materialCustomerContractService.insert(materialCustomerContract);
		}
	}
/*	private MaterialContractOperateTurnover buildTurnover(MaterialCustomerContract materialCustomerContract){
		MaterialContractOperateTurnover materialContractOperateTurnover = new MaterialContractOperateTurnover();
		materialContractOperateTurnover.setContractId(materialCustomerContract.getId());
		materialContractOperateTurnover.setOperateAccount(WebUtils.getLoggedUser().getLoginName());
		materialContractOperateTurnover.setOperateTime(new Date());
		materialContractOperateTurnover.setMaterialCustomerContract(materialCustomerContract);
		return materialContractOperateTurnover;
	}*/
}
