package com.mdni.scm.common.dto;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.mdni.scm.common.Constants;

public class MutipleDataStatusDto extends StatusDto<HashMap<String, Object>> {

	private MutipleDataStatusDto() {
	}

	private MutipleDataStatusDto(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static MutipleDataStatusDto buildMutipleDataDtoWithCode(String code, String message) {
		return new MutipleDataStatusDto(code, message);
	}

	public static MutipleDataStatusDto buildMutipleDataSuccessDto(String message) {
		return new MutipleDataStatusDto(Constants.RESP_STATUS_CODE_SUCCESS, message);
	}

	public static MutipleDataStatusDto buildMutipleDataSuccessDto() {
		return new MutipleDataStatusDto(Constants.RESP_STATUS_CODE_SUCCESS, SUCCESS_MSG);
	}

	public MutipleDataStatusDto append(String key, Object value) {
		if (value != null) {
			if (data == null) {
				data = Maps.newHashMap();
			}
			data.put(key, value);
		}
		return this;
	}
}
