package com.mdni.scm.web.filter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

/**
 * @author zhangmin
 */
public final class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	//需要转义的字符
	private String escape;

	//转义成目标字符
	private String escapeTo;

	//不需要经过xssFilter过滤的路径
	private List<String> excludeXssPathList;

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public XssHttpServletRequestWrapper(HttpServletRequest request, String escape, String escapeTo,
		List<String> excludeXssPathList) {
		super(request);
		this.escape = escape;
		this.escapeTo = escapeTo;
		this.excludeXssPathList = excludeXssPathList;
	}

	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
	 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	 */
	@Override
	public String getParameter(String name) {
		super.getParameter(name);
		String value = super.getParameter(name);
		if (isDoXssEscape()) {
			return xssEscape(value);
		}
		return value;
	}

	//是否需要执行转义
	private boolean isDoXssEscape() {
		if (CollectionUtils.isEmpty(excludeXssPathList)) {
			return true;
		}

		String servletPath = ((HttpServletRequest) getRequest()).getServletPath();
		if (excludeXssPathList.contains(servletPath)) {
			return false;
		}
		return true;
	}

	public String[] getParameterValues(String name) {
		String[] paramValues = super.getParameterValues(name);
		if (paramValues == null || !isDoXssEscape())
			return paramValues;

		int len = paramValues.length;
		String[] values = new String[len];
		for (int j = 0; j < len; j++)
			values[j] = xssEscape(paramValues[j]);
		return values;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramMap = super.getParameterMap();
		if (MapUtils.isNotEmpty(paramMap)) {
			Map<String, String[]> escapedMap = Maps.newLinkedHashMap();

			Iterator<String> paramNameIter = paramMap.keySet().iterator();
			while (paramNameIter.hasNext()) {
				final String paramName = paramNameIter.next();
				escapedMap.put(paramName, getParameterValues(paramName));
			}
			return escapedMap;
		}
		return paramMap;
	}

	/**
	 * 覆盖getHeader方法，参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
	 */
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		return xssEscape(value);
	}

	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符
	 */
	private String xssEscape(String paramValue) {
		if (StringUtils.isBlank(paramValue)) {
			return paramValue;
		}
		if (StringUtils.isNotBlank(escape) && StringUtils.isNotBlank(escapeTo) && escape.length() == escapeTo.length()) {
			char[] escapeChars = escape.toCharArray();
			char[] escapeToChars = escapeTo.toCharArray();
			StringBuilder sb = new StringBuilder(paramValue.length() + 16);
			for (int i = 0; i < paramValue.length(); i++) {
				char c = paramValue.charAt(i);

				int idx = ArrayUtils.indexOf(escapeChars, c);
				if (idx > -1) {
					sb.append(escapeToChars[idx]);
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} else {
			return paramValue;
		}
	}

	private String cleanXSS(String paramValue) {
		if (StringUtils.isBlank(paramValue)) {
			return paramValue;
		}

		final int flags = 42;
		String str = paramValue;
		Pattern pattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", flags);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", flags);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("<script(.*?)>", flags);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("eval\\((.*?)\\)", flags);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("expression\\((.*?)\\)", flags);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		pattern = Pattern.compile("onload(.*?)=", flags);
		str = pattern.matcher(str).replaceAll(StringUtils.EMPTY);
		str = str.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		str = str.replaceAll("'", "&#39;");
		str = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		return str;
	}
}
