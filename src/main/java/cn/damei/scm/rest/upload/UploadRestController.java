package cn.damei.scm.rest.upload;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.util.ClassUtil;
import cn.damei.scm.common.BaseController;
import cn.damei.scm.common.PropertyHolder;
import cn.damei.scm.common.dto.MutipleDataStatusDto;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.utils.ExcelUtil;
import cn.damei.scm.service.upload.UploadService;
import cn.damei.scm.service.upload.UploadService.UploadCategory;

@RestController
@RequestMapping(value = "/api/upload")
public class UploadRestController extends BaseController {

	@Autowired
	private UploadService uploadService;

	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public Object delete(@RequestParam String path) {
		uploadService.delete(path);
		return StatusDto.buildSuccessStatusDto();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Object upload(HttpServletRequest req, HttpServletResponse response, MultipartFile file,
		@RequestParam UploadCategory type) {

		String saveTmpPath = StringUtils.EMPTY;
		try {
			saveTmpPath = uploadService.upload(file, type);
		} catch (Exception e) {
			return StatusDto.buildFailureStatusDto(e.getMessage());
		}

		String imgPreviewPath = PropertyHolder.getFullImageUrl(saveTmpPath);
		MutipleDataStatusDto dto = MutipleDataStatusDto.buildMutipleDataSuccessDto();
		dto.append("fullPath", imgPreviewPath).append("path", saveTmpPath);
		return dto;

	}

	@RequestMapping(value = "/validateExcel", method = RequestMethod.GET)
	public Object validateExcel(String path) {
		try {
			File file = uploadService.getFileByPath(path);
			if (!file.exists()) {
				return StatusDto.buildFailureStatusDto("文件不存在");
			}
			UploadCategory category = uploadService.parseCatalogType(path);
			if (!category.getPath().toLowerCase().endsWith("import")) {
				return StatusDto.buildFailureStatusDto("不是excel类型");
			}
			if (category.getClassName() == null) {
				return StatusDto.buildFailureStatusDto("后台类型数据配置不全");
			}
			ExcelUtil.getInstance().readExcel2ObjsByFile(file, ClassUtil.findClass(category.getClassName()));
			return StatusDto.buildSuccessStatusDto("文件验证成功");
		} catch (Exception e) {
			return StatusDto.buildFailureStatusDto(e.getMessage());
		}
	}

}
