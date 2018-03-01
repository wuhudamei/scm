package com.damei.scm.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.service.ServiceException;
import com.damei.scm.common.utils.DateUtil;
import com.damei.scm.common.utils.StringTrimEscapeEditor;

@SuppressWarnings("all")
public abstract class BaseController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected boolean isEscapeHTMLHook() {
		return true;
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder
			.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DateUtil.DATE_PATTERN), true));
		binder.registerCustomEditor(String.class, new StringTrimEscapeEditor(isEscapeHTMLHook()));
	}

	@ExceptionHandler({QueryDataException.class})
	public Object queryDataException(Exception ex) {
		logger.error("数据查询异常:", ex);
		return StatusDto.buildFailureStatusDto("查询错误，请联系管理员！");
	}

	@ExceptionHandler({ServiceException.class})
	public Object serviceException(Exception ex) {
		logger.error("service error", ex);
		return StatusDto.buildFailureStatusDto(ex.getMessage());
	}

	@ExceptionHandler({Exception.class})
	public Object exception(Exception ex) {
		logger.error("异常:", ex);
		return StatusDto.buildFailureStatusDto("操作失败");
	}

}
