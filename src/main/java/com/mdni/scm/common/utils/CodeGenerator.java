package com.mdni.scm.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author zhangmin
 */
public final class CodeGenerator {

	/**
	 * 生成业务流水号/申请单号: bussniessPrefix + yyMMddHHmmss + 8位随机数字
	 * 
	 * @param bussniessPrefix 业务代码前缀,可以为空
	 */
	public static String generateCode(String bussniessPrefix) {
		final String datePart = com.mdni.scm.common.utils.DateUtil.formatDate(new Date(),
			com.mdni.scm.common.utils.DateUtil.YYMMDDHHMMSS_PATTERN);
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(bussniessPrefix)) {
			sb.append(bussniessPrefix);
		}
		sb.append(datePart);
		String randomPart = RandomTools.getRandStr(8, 2);
		sb.append(randomPart);
		return sb.toString();
	}
}
