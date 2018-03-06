package cn.damei.scm.common.dto;

import cn.damei.scm.common.Constants;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusDto<T> {

	public static final String SUCCESS_MSG = "成功";
	public static final String FAIL_MSG = "失败";

	protected String code;
	protected String message;

	protected T data;

	public static StatusDto<String> buildSuccessStatusDto() {
		return buildSuccessStatusDto(SUCCESS_MSG);
	}

	public static StatusDto<String> buildSuccessStatusDto(String message) {
		return buildStatusDtoWithCode(Constants.RESP_STATUS_CODE_SUCCESS, message);
	}

	public static <E> StatusDto<E> buildDataSuccessStatusDto(String message, E data) {
		StatusDto<E> dto = new StatusDto<E>();
		dto.code = Constants.RESP_STATUS_CODE_SUCCESS;
		dto.message = message;
		dto.data = data;
		return dto;
	}

	public static <E> StatusDto<E> buildDataSuccessStatusDto(E data) {
		return buildDataSuccessStatusDto(SUCCESS_MSG, data);
	}

	/**
	 * 自定义成功的code
	 * 
	 * @param code code
	 * @param message 信息
	 */
	public static StatusDto<String> buildStatusDtoWithCode(String code, String message) {
		StatusDto<String> dto = new StatusDto<String>();
		dto.code = code;
		dto.message = message;
		return dto;
	}

	public static StatusDto<String> buildFailureStatusDto() {
		return buildFailureStatusDto(FAIL_MSG);
	}

	public static StatusDto<String> buildFailureStatusDto(String message) {
		return buildStatusDtoWithCode(Constants.RESP_STATUS_CODE_FAIL, message);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return StringUtils.equals(this.code, Constants.RESP_STATUS_CODE_SUCCESS);
	}

}