package cn.damei.scm.rest.dataArrangement;

import cn.damei.scm.common.utils.*;
import cn.damei.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.Constants;
import cn.damei.scm.common.dto.BootstrapPage;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.entity.dataArrangement.MaterialCustomerContract;
import cn.damei.scm.entity.eum.dataArrangement.MetarialContractStatusEnum;
import cn.damei.scm.service.dataArrangement.MaterialCustomerContractService;
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

@RestController
@RequestMapping("/api/customerContract")
public class MaterialCustomerContractController extends BaseComController<MaterialCustomerContractService, MaterialCustomerContract> {
	public static final String Y = "是";
	public static final String N = "否";
	@Autowired
	private MaterialCustomerContractService materialCustomerContractService;

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

	@RequestMapping(value = "/saveConfirmData", method = RequestMethod.POST)
	public Object saveConfirmData(@RequestBody MaterialCustomerContractConfirm materialCustomerContractConfirm){
		this.materialCustomerContractService.saveConfirmData(materialCustomerContractConfirm);
		return StatusDto.buildDataSuccessStatusDto("操作成功！");
	}

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
		String errorMsg = validateImportExcelProductData(productDtoList);
		if (StringUtils.isNotEmpty(errorMsg)) {
			return StatusDto.buildFailureStatusDto(errorMsg);
		}
		executeEncapsulateProduct(productDtoList);
		return StatusDto.buildSuccessStatusDto("商品批量导入成功");
	}
	private String validateImportExcelProductData(List<MaterialCustomerContract> customerContractList) {

		int rowIndex = 1;
		String errorMsg = null;

		final String rowNumKey = "rowNum";
		final String errorMsgKey = "errorMsg";
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
				String exportBudgetFee = productDto.getExportBudgetFee();
				if(!StringUtils.isEmpty(exportBudgetFee)) {
					new BigDecimal(exportBudgetFee);
				}
			}catch (Exception e){
				errorBuf.append("工程预算数值有问题;");
			}
			rowIndex++;
			if (errorBuf.length() > 0) {
				break;
			}
		}
		if (errorBuf.length() > 0) {
			errorMsg = errorMsgTmp.replace(rowNumKey, String.valueOf(rowIndex)).replace(errorMsgKey,
					errorBuf.toString());
		}
		return errorMsg;
	}

	private void executeEncapsulateProduct(List<MaterialCustomerContract> customerContractList) {
		for (MaterialCustomerContract materialCustomerContract:customerContractList) {
			materialCustomerContract.setCreateTime(new Date());
			materialCustomerContract.setCreateAccount(WebUtils.getLoggedUser().getLoginName());
			String exportHaveElevator = materialCustomerContract.getExportHaveElevator();
			if(!StringUtils.isEmpty(exportHaveElevator)){
				if(exportHaveElevator.equals(Y)){
					materialCustomerContract.setHaveElevator(1);
				}else if(exportHaveElevator.equals(N)){
					materialCustomerContract.setHaveElevator(0);
				}
			}
			String exportBudgetFee = materialCustomerContract.getExportBudgetFee();
			if(!StringUtils.isEmpty(exportBudgetFee)) {
				materialCustomerContract.setBudgetFee(new BigDecimal(exportBudgetFee));
			}
			Date exportContractSignDate = materialCustomerContract.getExportContractSignDate();
			if(exportContractSignDate!=null) {
				materialCustomerContract.setContractSignDate(DateUtil.parseToDate(DateUtil.formatDate(exportContractSignDate)));
			}
			materialCustomerContract.setContractStatus(MetarialContractStatusEnum.NOTCHECKED);
			materialCustomerContractService.insert(materialCustomerContract);
		}
	}
}
