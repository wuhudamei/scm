package com.mdni.scm.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.service.ServiceException;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.common.utils.StringTrimEscapeEditor;

/**
 * <dl>
 * <dd>描述:</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：15/8/11 下午3:47</dd>
 * <dd>创建人： weiys</dd>
 * </dl>
 */
@SuppressWarnings("all")
public abstract class BaseController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	//是否需要转移HTML 的钩子方法
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
